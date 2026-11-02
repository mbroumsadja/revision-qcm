package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "qcm_documents")
data class QcmDocumentEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val bestScore: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "qcm_questions",
    foreignKeys = [
        ForeignKey(
            entity = QcmDocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["documentId"])]
)
data class QcmQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val documentId: Long,
    val number: Int,
    val text: String,
    val correctLetter: String? = null,
    val explanation: String = "",
    val optionsJson: String // serialized JSON array of options
)

@Entity(
    tableName = "quiz_score_records",
    foreignKeys = [
        ForeignKey(
            entity = QcmDocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["documentId"])]
)
data class QuizScoreRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val documentId: Long,
    val documentTitle: String,
    val mode: String, // LEARN, EXAM, PACE
    val scorePercent: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val unansweredCount: Int,
    val totalQuestions: Int,
    val elapsedSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)
