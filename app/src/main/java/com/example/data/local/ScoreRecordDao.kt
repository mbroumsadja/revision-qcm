package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoreRecord(record: QuizScoreRecordEntity): Long

    @Query("SELECT * FROM quiz_score_records ORDER BY timestamp DESC")
    fun getAllScoresFlow(): Flow<List<QuizScoreRecordEntity>>

    @Query("SELECT * FROM quiz_score_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentScoresFlow(limit: Int = 20): Flow<List<QuizScoreRecordEntity>>

    @Query("SELECT * FROM quiz_score_records WHERE documentId = :documentId ORDER BY timestamp DESC")
    fun getScoresForDocumentFlow(documentId: Long): Flow<List<QuizScoreRecordEntity>>

    @Query("SELECT * FROM quiz_score_records WHERE documentId = :documentId ORDER BY timestamp DESC")
    suspend fun getScoresForDocument(documentId: Long): List<QuizScoreRecordEntity>

    @Query("SELECT COUNT(*) FROM quiz_score_records WHERE documentId = :documentId")
    suspend fun getAttemptCountForDocument(documentId: Long): Int

    @Query("DELETE FROM quiz_score_records WHERE id = :id")
    suspend fun deleteScoreRecord(id: Long)

    @Query("DELETE FROM quiz_score_records WHERE documentId = :documentId")
    suspend fun deleteScoresForDocument(documentId: Long)
}
