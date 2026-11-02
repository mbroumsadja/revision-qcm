package com.example.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QcmRepository
import com.example.data.local.QuizScoreRecordEntity
import com.example.model.QcmDocument
import com.example.model.QcmOption
import com.example.model.QcmQuestion
import com.example.model.QuizConfig
import com.example.model.QuizMode
import com.example.model.QuizResult
import com.example.model.SessionItem
import com.example.parser.PdfTextExtractor
import com.example.parser.QcmParser
import com.example.parser.SampleDecks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

enum class MainTab(val label: String) {
    HOME("Home"),
    HISTORIQUE("Historique"),
    RESULTATS("Résultats");

    companion object {
        val QCM = HOME
    }
}

sealed interface Screen {
    data object Home : Screen
    data object Setup : Screen
    data object Quiz : Screen
}

data class UiState(
    val currentScreen: Screen = Screen.Home,
    val selectedTab: MainTab = MainTab.HOME,
    val documents: List<QcmDocument> = emptyList(),
    val scoreHistory: List<QuizScoreRecordEntity> = emptyList(),
    val selectedDoc: QcmDocument? = null,
    val config: QuizConfig = QuizConfig(),
    // Active quiz session state
    val sessionItems: List<SessionItem> = emptyList(),
    val currentIndex: Int = 0,
    val answers: Map<Int, Int> = emptyMap(), // questionIndex -> selectedOptionIndex (-1 if unanswered)
    val timeSpentSeconds: Map<Int, Double> = emptyMap(),
    val elapsedTotalSeconds: Double = 0.0,
    val examRemainingSeconds: Double = 0.0,
    val paceRemainingSeconds: Double = 0.0,
    val isTimerLow: Boolean = false,
    val quizResult: QuizResult? = null,
    val resultFilter: String = "bad", // "bad" or "all"
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val isImporting: Boolean = false,
    val isDarkMode: Boolean? = null // null = follows system theme, true/false = manual toggle
)

class QcmViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QcmRepository(application)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var sessionStartTime = 0L
    private var questionStartTime = 0L
    private var examTotalSeconds = 0

    fun toggleDarkMode(systemDark: Boolean) {
        _uiState.update { state ->
            val current = state.isDarkMode ?: systemDark
            state.copy(isDarkMode = !current)
        }
    }

    init {
        loadDocuments()
        observeScores()
        checkAndSeedInitialDeck()
    }

    private fun observeScores() {
        viewModelScope.launch {
            repository.getAllScoresFlow().collect { records ->
                _uiState.update { it.copy(scoreHistory = records) }
            }
        }
    }

    private fun checkAndSeedInitialDeck() {
        viewModelScope.launch {
            val prefs = getApplication<Application>().getSharedPreferences("qcm_prefs", Context.MODE_PRIVATE)
            val alreadySeeded = prefs.getBoolean("has_seeded_10_test_qcm", false)
            if (!alreadySeeded) {
                val currentDocs = repository.loadDocuments()
                if (currentDocs.isEmpty()) {
                    repository.saveDocument(SampleDecks.get10TestQcmDocument())
                    loadDocuments()
                }
                prefs.edit().putBoolean("has_seeded_10_test_qcm", true).apply()
            }
        }
    }

    fun generateTestQcmDeck() {
        viewModelScope.launch {
            val testDoc = SampleDecks.get10TestQcmDocument()
            repository.saveDocument(testDoc)
            loadDocuments()
            _uiState.update {
                it.copy(
                    infoMessage = "10 QCM de test générés avec succès !",
                    errorMessage = null
                )
            }
        }
    }

    fun loadDocuments() {
        viewModelScope.launch {
            val docs = repository.loadDocuments()
            _uiState.update { it.copy(documents = docs) }
        }
    }

    fun openSetup(doc: QcmDocument) {
        stopTimer()
        _uiState.update {
            val defaultCount = 20.coerceAtMost(doc.questions.size.coerceAtLeast(1))
            it.copy(
                selectedDoc = doc,
                config = it.config.copy(questionCount = defaultCount),
                currentScreen = Screen.Setup,
                errorMessage = null,
                infoMessage = null
            )
        }
    }

    fun selectTab(tab: MainTab) {
        stopTimer()
        _uiState.update {
            it.copy(
                selectedTab = tab,
                currentScreen = Screen.Home,
                errorMessage = null,
                infoMessage = null
            )
        }
    }

    fun deleteDocument(docId: Long) {
        viewModelScope.launch {
            repository.deleteDocument(docId)
            loadDocuments()
        }
    }

    fun deleteAllDocuments() {
        viewModelScope.launch {
            repository.deleteAllDocuments()
            loadDocuments()
        }
    }

    fun updateConfig(newConfig: QuizConfig) {
        _uiState.update { it.copy(config = newConfig) }
    }

    fun setMode(mode: QuizMode) {
        _uiState.update { it.copy(config = it.config.copy(mode = mode)) }
    }

    fun setQuestionCount(count: Int) {
        _uiState.update { it.copy(config = it.config.copy(questionCount = count)) }
    }

    fun setPaceSeconds(seconds: Int) {
        _uiState.update { it.copy(config = it.config.copy(secondsPerQuestion = seconds.coerceIn(5, 300))) }
    }

    fun setExamMinutes(minutes: Int) {
        _uiState.update { it.copy(config = it.config.copy(examTotalMinutes = minutes.coerceIn(0, 300))) }
    }

    fun setShuffleQuestions(shuffle: Boolean) {
        _uiState.update { it.copy(config = it.config.copy(shuffleQuestions = shuffle)) }
    }

    fun setShuffleOptions(shuffle: Boolean) {
        _uiState.update { it.copy(config = it.config.copy(shuffleOptions = shuffle)) }
    }

    fun startQuiz(customQuestions: List<QcmQuestion>? = null) {
        val currentDoc = _uiState.value.selectedDoc ?: return
        val cfg = _uiState.value.config

        var pool: List<QcmQuestion> = customQuestions ?: currentDoc.questions.filter { it.correctLetter != null }
        if (pool.isEmpty()) {
            pool = currentDoc.questions
        }

        if (customQuestions == null) {
            if (cfg.shuffleQuestions) {
                pool = pool.shuffled()
            }
            if (cfg.questionCount in 1 until pool.size) {
                pool = pool.take(cfg.questionCount)
            }
        }

        val items = pool.map { q ->
            var opts = q.options.map { o ->
                val ok = if (q.correctLetter != null) o.letter.equals(q.correctLetter, ignoreCase = true) else o.isCorrect
                QcmOption(o.letter, o.text, ok)
            }
            if (cfg.shuffleOptions) {
                opts = opts.shuffled()
            }
            val alphabet = listOf("A", "B", "C", "D", "E")
            val remapped = opts.mapIndexed { idx, opt ->
                val let = if (idx < alphabet.size) alphabet[idx] else opt.letter
                opt.copy(letter = let)
            }
            SessionItem(
                source = q,
                number = q.number,
                text = q.text,
                options = remapped,
                explanation = q.explanation
            )
        }

        if (items.isEmpty()) return

        stopTimer()

        val totalMinutes = if (cfg.examTotalMinutes > 0) cfg.examTotalMinutes else Math.ceil(items.size * 0.75).toInt()
        examTotalSeconds = totalMinutes * 60

        val now = System.currentTimeMillis()
        sessionStartTime = now
        questionStartTime = now

        _uiState.update {
            it.copy(
                currentScreen = Screen.Quiz,
                sessionItems = items,
                currentIndex = 0,
                answers = emptyMap(),
                timeSpentSeconds = emptyMap(),
                elapsedTotalSeconds = 0.0,
                examRemainingSeconds = examTotalSeconds.toDouble(),
                paceRemainingSeconds = cfg.secondsPerQuestion.toDouble(),
                isTimerLow = false,
                quizResult = null,
                errorMessage = null
            )
        }

        startTimer()
    }

    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(200)
                val now = System.currentTimeMillis()
                val elapsedTotal = (now - sessionStartTime) / 1000.0
                val mode = _uiState.value.config.mode

                when (mode) {
                    QuizMode.LEARN -> {
                        _uiState.update {
                            it.copy(
                                elapsedTotalSeconds = elapsedTotal,
                                isTimerLow = false
                            )
                        }
                    }
                    QuizMode.EXAM -> {
                        val rem = examTotalSeconds - elapsedTotal
                        if (rem <= 0) {
                            finishQuiz()
                            break
                        }
                        val isLow = rem / examTotalSeconds < 0.15
                        _uiState.update {
                            it.copy(
                                examRemainingSeconds = rem,
                                isTimerLow = isLow
                            )
                        }
                    }
                    QuizMode.PACE -> {
                        val paceTotal = _uiState.value.config.secondsPerQuestion
                        val qElapsed = (now - questionStartTime) / 1000.0
                        val rem = paceTotal - qElapsed
                        if (rem <= 0) {
                            goToNextOrFinish()
                        } else {
                            val isLow = rem / paceTotal < 0.20
                            _uiState.update {
                                it.copy(
                                    paceRemainingSeconds = rem,
                                    isTimerLow = isLow
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun selectOption(optionIndex: Int) {
        val state = _uiState.value
        val curIdx = state.currentIndex
        val mode = state.config.mode

        if (mode != QuizMode.EXAM && state.answers.containsKey(curIdx)) {
            return
        }

        val updatedAnswers = state.answers.toMutableMap()
        updatedAnswers[curIdx] = optionIndex
        _uiState.update { it.copy(answers = updatedAnswers) }

        if (mode == QuizMode.PACE) {
            viewModelScope.launch {
                delay(350)
                if (_uiState.value.currentScreen == Screen.Quiz && _uiState.value.currentIndex == curIdx) {
                    goToNextOrFinish()
                }
            }
        }
    }

    fun goToQuestion(targetIndex: Int) {
        val state = _uiState.value
        if (targetIndex < 0 || targetIndex >= state.sessionItems.size) return

        recordCurrentQuestionTime()
        questionStartTime = System.currentTimeMillis()

        _uiState.update {
            it.copy(
                currentIndex = targetIndex,
                paceRemainingSeconds = it.config.secondsPerQuestion.toDouble()
            )
        }
    }

    private fun goToNextOrFinish() {
        val state = _uiState.value
        val next = state.currentIndex + 1
        if (next < state.sessionItems.size) {
            goToQuestion(next)
        } else {
            finishQuiz()
        }
    }

    private fun recordCurrentQuestionTime() {
        val now = System.currentTimeMillis()
        val curIdx = _uiState.value.currentIndex
        val qDuration = (now - questionStartTime) / 1000.0
        val currentSpent = _uiState.value.timeSpentSeconds.toMutableMap()
        val prev = currentSpent[curIdx] ?: 0.0
        currentSpent[curIdx] = prev + qDuration
        _uiState.update { it.copy(timeSpentSeconds = currentSpent) }
    }

    fun finishQuiz() {
        stopTimer()
        recordCurrentQuestionTime()

        val state = _uiState.value
        val items = state.sessionItems
        val answers = state.answers

        var correctCount = 0
        var unansweredCount = 0

        for (i in items.indices) {
            val userPick = answers[i]
            if (userPick == null || userPick < 0) {
                unansweredCount++
            } else if (items[i].options.getOrNull(userPick)?.isCorrect == true) {
                correctCount++
            }
        }

        val total = items.size
        val incorrectCount = total - correctCount - unansweredCount
        val percentage = if (total > 0) Math.round((correctCount.toDouble() / total) * 100).toInt() else 0

        val totalElapsedSec = state.timeSpentSeconds.values.sum().toInt()
        val avgPerQ = if (total > 0) totalElapsedSec / total else 0

        val result = QuizResult(
            correctCount = correctCount,
            incorrectCount = incorrectCount,
            unansweredCount = unansweredCount,
            percentage = percentage,
            elapsedSeconds = totalElapsedSec,
            averageSecondsPerQuestion = avgPerQ
        )

        val doc = state.selectedDoc
        if (doc != null) {
            viewModelScope.launch {
                repository.saveScoreRecord(
                    docId = doc.id,
                    docTitle = doc.title,
                    mode = state.config.mode.label,
                    scorePercent = percentage,
                    correctCount = correctCount,
                    incorrectCount = incorrectCount,
                    unansweredCount = unansweredCount,
                    totalQuestions = total,
                    elapsedSeconds = totalElapsedSec
                )
                loadDocuments()
            }
        }

        _uiState.update {
            it.copy(
                currentScreen = Screen.Home,
                selectedTab = MainTab.RESULTATS,
                quizResult = result,
                resultFilter = if (correctCount < total) "bad" else "all"
            )
        }
    }

    fun setResultFilter(filter: String) {
        _uiState.update { it.copy(resultFilter = filter) }
    }

    fun retryMistakes() {
        val state = _uiState.value
        val wrongItems = state.sessionItems.filterIndexed { index, item ->
            val userAns = state.answers[index]
            userAns == null || userAns < 0 || item.options.getOrNull(userAns)?.isCorrect != true
        }.map { it.source }

        if (wrongItems.isNotEmpty()) {
            startQuiz(customQuestions = wrongItems)
        }
    }

    fun returnToHome() {
        stopTimer()
        _uiState.update {
            it.copy(
                currentScreen = Screen.Home,
                selectedTab = MainTab.HISTORIQUE,
                sessionItems = emptyList(),
                errorMessage = null,
                infoMessage = null
            )
        }
    }

    fun returnToSetup() {
        stopTimer()
        _uiState.update {
            it.copy(
                currentScreen = Screen.Setup,
                sessionItems = emptyList(),
                quizResult = null,
                errorMessage = null
            )
        }
    }

    fun importFromText(title: String, rawText: String) {
        _uiState.update { it.copy(isImporting = true, errorMessage = null, infoMessage = null) }
        viewModelScope.launch {
            val result = repository.importText(title, rawText)
            result.onSuccess { doc ->
                loadDocuments()
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        selectedDoc = doc,
                        currentScreen = Screen.Home,
                        selectedTab = MainTab.HISTORIQUE,
                        infoMessage = "Document « ${doc.title} » importé avec succès (${doc.questions.size} questions enregistrées) !"
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        errorMessage = err.message ?: "Erreur lors de l'analyse du texte QCM."
                    )
                }
            }
        }
    }

    fun importFromFileUri(context: Context, uri: Uri) {
        _uiState.update { it.copy(isImporting = true, errorMessage = null, infoMessage = null) }
        viewModelScope.launch {
            try {
                val contentResolver = context.contentResolver
                val fileName = getFileName(context, uri) ?: "Document importé"
                val mimeType = contentResolver.getType(uri) ?: ""
                val isPdf = fileName.endsWith(".pdf", ignoreCase = true) || mimeType.contains("pdf", ignoreCase = true)

                val rawText = contentResolver.openInputStream(uri)?.use { stream ->
                    if (isPdf) {
                        PdfTextExtractor.extractText(context, stream)
                    } else {
                        val stringBuilder = StringBuilder()
                        BufferedReader(InputStreamReader(stream)).use { reader ->
                            var line: String? = reader.readLine()
                            while (line != null) {
                                stringBuilder.append(line).append("\n")
                                line = reader.readLine()
                            }
                        }
                        stringBuilder.toString()
                    }
                } ?: ""
                if (rawText.isBlank()) {
                    _uiState.update {
                        it.copy(
                            isImporting = false,
                            errorMessage = "Le fichier sélectionné est vide."
                        )
                    }
                    return@launch
                }

                val cleanTitle = fileName.replace(Regex("""\.[a-zA-Z0-9]+$"""), "").replace(Regex("""[_-]+"""), " ")
                val result = repository.importText(cleanTitle, rawText)

                result.onSuccess { doc ->
                    loadDocuments()
                    _uiState.update {
                        it.copy(
                            isImporting = false,
                            selectedDoc = doc,
                            currentScreen = Screen.Home,
                            selectedTab = MainTab.HISTORIQUE,
                            infoMessage = "Document « ${doc.title} » importé avec succès (${doc.questions.size} questions enregistrées) !"
                        )
                    }
                }.onFailure { err ->
                    _uiState.update {
                        it.copy(
                            isImporting = false,
                            errorMessage = "${err.message} Vous pouvez également copier/coller le texte directement."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        errorMessage = "Impossible de lire le fichier (${e.localizedMessage ?: "format non supporté"}). Utilisez l'option 'Coller le texte'."
                    )
                }
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) {
                        name = it.getString(index)
                    }
                }
            }
        }
        if (name == null) {
            name = uri.path?.let { p ->
                val cut = p.lastIndexOf('/')
                if (cut != -1) p.substring(cut + 1) else p
            }
        }
        return name
    }

    fun dismissMessages() {
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }
}
