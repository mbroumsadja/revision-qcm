package com.example.model

enum class QuizMode(val label: String, val description: String) {
    LEARN("Apprentissage", "Correction et explication après chaque question, sans limite de temps."),
    EXAM("Examen", "Temps total limité, navigation libre, correction à la fin."),
    PACE("Rythmé", "Un temps fixe par question, enchaînement automatique.")
}

data class QcmOption(
    val letter: String,
    val text: String,
    val isCorrect: Boolean = false
)

data class QcmQuestion(
    val number: Int,
    val text: String,
    val options: List<QcmOption>,
    val correctLetter: String? = null,
    val explanation: String = ""
)

data class QcmDocument(
    val id: Long,
    val title: String,
    val questions: List<QcmQuestion>,
    val bestScore: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class QuizConfig(
    val mode: QuizMode = QuizMode.LEARN,
    val questionCount: Int = 20, // default 20 QCMs
    val shuffleQuestions: Boolean = true,
    val shuffleOptions: Boolean = true,
    val secondsPerQuestion: Int = 45,
    val examTotalMinutes: Int = 0 // 0 = auto (45s per question)
)

data class SessionItem(
    val source: QcmQuestion,
    val number: Int,
    val text: String,
    val options: List<QcmOption>,
    val explanation: String
)

data class QuizResult(
    val correctCount: Int,
    val incorrectCount: Int,
    val unansweredCount: Int,
    val percentage: Int,
    val elapsedSeconds: Int,
    val averageSecondsPerQuestion: Int
)
