package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        QcmDocumentEntity::class,
        QcmQuestionEntity::class,
        QuizScoreRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class QcmDatabase : RoomDatabase() {

    abstract fun qcmDao(): QcmDao
    abstract fun scoreRecordDao(): ScoreRecordDao

    companion object {
        @Volatile
        private var INSTANCE: QcmDatabase? = null

        fun getInstance(context: Context): QcmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QcmDatabase::class.java,
                    "qcm_revision.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
