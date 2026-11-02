package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.QcmRepository
import com.example.model.QcmDocument
import com.example.model.QcmOption
import com.example.model.QcmQuestion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Révision QCM", appName)
  }

  @Test
  fun `repository has zero documents on fresh launch before any upload`() = runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = QcmRepository(context)
    repository.deleteAllDocuments()
    val docs = repository.loadDocuments()
    assertTrue("La liste des documents doit être à zéro avant tout téléversement", docs.isEmpty())
  }

  @Test
  fun `save and retrieve score in sqlite database`() = runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = QcmRepository(context)

    val testDoc = QcmDocument(
      id = 999L,
      title = "Test QCM",
      questions = listOf(
        QcmQuestion(
          number = 1,
          text = "Test question",
          options = listOf(
            QcmOption("A", "Choix A", true),
            QcmOption("B", "Choix B", false)
          ),
          correctLetter = "A",
          explanation = "Explication test"
        )
      )
    )
    repository.saveDocument(testDoc)

    val docs = repository.loadDocuments()
    assertTrue(docs.isNotEmpty())
    val firstDoc = docs.first()

    val recordId = repository.saveScoreRecord(
      docId = firstDoc.id,
      docTitle = firstDoc.title,
      mode = "Examen",
      scorePercent = 90,
      correctCount = 1,
      incorrectCount = 0,
      unansweredCount = 0,
      totalQuestions = 1,
      elapsedSeconds = 30
    )
    assertTrue(recordId > 0)

    val scores = repository.getAllScoresFlow().first()
    assertTrue(scores.any { it.documentId == firstDoc.id && it.scorePercent == 90 })
  }
}
