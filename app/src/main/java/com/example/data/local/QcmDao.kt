package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface QcmDao {

    @Query("SELECT * FROM qcm_documents ORDER BY createdAt DESC")
    fun getAllDocumentsFlow(): Flow<List<QcmDocumentEntity>>

    @Query("SELECT * FROM qcm_documents ORDER BY createdAt DESC")
    suspend fun getAllDocuments(): List<QcmDocumentEntity>

    @Query("SELECT * FROM qcm_documents WHERE id = :id LIMIT 1")
    suspend fun getDocumentById(id: Long): QcmDocumentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: QcmDocumentEntity): Long

    @Query("DELETE FROM qcm_documents WHERE id = :id")
    suspend fun deleteDocument(id: Long)

    @Query("DELETE FROM qcm_documents")
    suspend fun deleteAllDocuments()

    @Query("UPDATE qcm_documents SET bestScore = :bestScore WHERE id = :docId")
    suspend fun updateBestScore(docId: Long, bestScore: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QcmQuestionEntity>)

    @Query("SELECT * FROM qcm_questions WHERE documentId = :documentId ORDER BY number ASC")
    suspend fun getQuestionsForDocument(documentId: Long): List<QcmQuestionEntity>

    @Query("DELETE FROM qcm_questions WHERE documentId = :documentId")
    suspend fun deleteQuestionsForDocument(documentId: Long)

    @Transaction
    suspend fun insertDocumentWithQuestions(
        doc: QcmDocumentEntity,
        questions: List<QcmQuestionEntity>
    ) {
        insertDocument(doc)
        deleteQuestionsForDocument(doc.id)
        insertQuestions(questions)
    }
}
