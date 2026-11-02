package com.example.data

import android.content.Context
import com.example.data.local.QcmDatabase
import com.example.data.local.QcmDocumentEntity
import com.example.data.local.QcmQuestionEntity
import com.example.data.local.QuizScoreRecordEntity
import com.example.model.QcmDocument
import com.example.model.QcmOption
import com.example.model.QcmQuestion
import com.example.parser.QcmParser
import com.example.parser.SampleDecks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class QcmRepository(context: Context) {

    private val db = QcmDatabase.getInstance(context)
    private val qcmDao = db.qcmDao()
    private val scoreDao = db.scoreRecordDao()

    suspend fun loadDocuments(): List<QcmDocument> = withContext(Dispatchers.IO) {
        val entities = qcmDao.getAllDocuments()
        if (entities.isEmpty()) {
            return@withContext emptyList()
        }

        entities.map { docEntity ->
            val qEntities = qcmDao.getQuestionsForDocument(docEntity.id)
            val questions = qEntities.map { qEnt ->
                QcmQuestion(
                    number = qEnt.number,
                    text = qEnt.text,
                    options = deserializeOptions(qEnt.optionsJson),
                    correctLetter = qEnt.correctLetter,
                    explanation = qEnt.explanation
                )
            }
            QcmDocument(
                id = docEntity.id,
                title = docEntity.title,
                questions = questions,
                bestScore = docEntity.bestScore,
                createdAt = docEntity.createdAt
            )
        }
    }

    suspend fun saveDocument(doc: QcmDocument) = withContext(Dispatchers.IO) {
        val docEntity = QcmDocumentEntity(
            id = doc.id,
            title = doc.title,
            bestScore = doc.bestScore,
            createdAt = doc.createdAt
        )
        val qEntities = doc.questions.map { q ->
            QcmQuestionEntity(
                documentId = doc.id,
                number = q.number,
                text = q.text,
                correctLetter = q.correctLetter,
                explanation = q.explanation,
                optionsJson = serializeOptions(q.options)
            )
        }
        qcmDao.insertDocumentWithQuestions(docEntity, qEntities)
    }

    suspend fun deleteDocument(docId: Long) = withContext(Dispatchers.IO) {
        qcmDao.deleteDocument(docId)
    }

    suspend fun deleteAllDocuments() = withContext(Dispatchers.IO) {
        qcmDao.deleteAllDocuments()
    }

    suspend fun updateBestScore(docId: Long, scorePercent: Int) = withContext(Dispatchers.IO) {
        val doc = qcmDao.getDocumentById(docId)
        if (doc != null) {
            val newBest = if (doc.bestScore == null || scorePercent > doc.bestScore) scorePercent else doc.bestScore
            qcmDao.updateBestScore(docId, newBest)
        }
    }

    suspend fun saveScoreRecord(
        docId: Long,
        docTitle: String,
        mode: String,
        scorePercent: Int,
        correctCount: Int,
        incorrectCount: Int,
        unansweredCount: Int,
        totalQuestions: Int,
        elapsedSeconds: Int
    ): Long = withContext(Dispatchers.IO) {
        val record = QuizScoreRecordEntity(
            documentId = docId,
            documentTitle = docTitle,
            mode = mode,
            scorePercent = scorePercent,
            correctCount = correctCount,
            incorrectCount = incorrectCount,
            unansweredCount = unansweredCount,
            totalQuestions = totalQuestions,
            elapsedSeconds = elapsedSeconds,
            timestamp = System.currentTimeMillis()
        )
        val recordId = scoreDao.insertScoreRecord(record)
        updateBestScore(docId, scorePercent)
        recordId
    }

    fun getAllScoresFlow(): Flow<List<QuizScoreRecordEntity>> {
        return scoreDao.getAllScoresFlow()
    }

    fun getScoresForDocumentFlow(docId: Long): Flow<List<QuizScoreRecordEntity>> {
        return scoreDao.getScoresForDocumentFlow(docId)
    }

    suspend fun importText(title: String, rawText: String): Result<QcmDocument> = withContext(Dispatchers.IO) {
        val questions = QcmParser.parse(rawText)
        if (questions.size < 2) {
            return@withContext Result.failure(
                IllegalArgumentException("Aucune question détectée. Le format attendu est « 1. Question / A. … / D. … » avec un corrigé.")
            )
        }

        val doc = QcmDocument(
            id = System.currentTimeMillis(),
            title = title.ifBlank { "Document importé" },
            questions = questions,
            bestScore = null,
            createdAt = System.currentTimeMillis()
        )
        saveDocument(doc)
        Result.success(doc)
    }

    private fun serializeOptions(options: List<QcmOption>): String {
        val arr = JSONArray()
        for (opt in options) {
            val obj = JSONObject().apply {
                put("letter", opt.letter)
                put("text", opt.text)
                put("isCorrect", opt.isCorrect)
            }
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun deserializeOptions(json: String): List<QcmOption> {
        val list = mutableListOf<QcmOption>()
        try {
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    QcmOption(
                        letter = obj.getString("letter"),
                        text = obj.getString("text"),
                        isCorrect = obj.optBoolean("isCorrect", false)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
