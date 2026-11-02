package com.example

import com.example.parser.QcmParser
import com.example.parser.SampleDecks
import com.example.parser.UltrasoundCourseDeck
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun parse_sample_qcm_text() {
        val questions = QcmParser.parse(SampleDecks.samplePasteText)
        assertEquals(3, questions.size)

        val q1 = questions[0]
        assertEquals(1, q1.number)
        assertEquals("Quelle est la capitale de l'Australie ?", q1.text)
        assertEquals(4, q1.options.size)
        assertEquals("C", q1.correctLetter)
        assertTrue(q1.options.first { it.letter == "C" }.isCorrect)
        assertTrue(q1.explanation.contains("Canberra"))

        val q2 = questions[1]
        assertEquals(2, q2.number)
        assertEquals("B", q2.correctLetter)
        assertTrue(q2.explanation.contains("Becquerel"))
    }

    @Test
    fun parse_ultrasound_course_100_questions() {
        val doc = UltrasoundCourseDeck.getDocument()
        assertEquals(100, doc.questions.size)

        val q1 = doc.questions[0]
        assertEquals(1, q1.number)
        assertEquals("C", q1.correctLetter)
        assertTrue(q1.options.first { it.letter == "C" }.isCorrect)
        assertTrue(q1.explanation.contains("ionisant"))

        val q100 = doc.questions[99]
        assertEquals(100, q100.number)
        assertEquals("D", q100.correctLetter)
        assertTrue(q100.options.first { it.letter == "D" }.isCorrect)
        assertTrue(q100.explanation.contains("air"))
    }

    @Test
    fun sample_decks_have_valid_questions() {
        val defaultDecks = SampleDecks.getDefaultDecks()
        assertTrue(defaultDecks.size >= 3)
        defaultDecks.forEach { deck ->
            assertTrue(deck.questions.isNotEmpty())
            deck.questions.forEach { q ->
                assertNotNull(q.correctLetter)
                assertTrue(q.options.any { it.isCorrect })
            }
        }
    }
}
