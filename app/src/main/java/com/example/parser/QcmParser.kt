package com.example.parser

import com.example.model.QcmOption
import com.example.model.QcmQuestion
import java.util.regex.Pattern

object QcmParser {

    /**
     * Parses QCM text with full support for courses formatted as:
     * - Numbered questions: 1. Question...
     * - Choices: A. ... B. ... C. ... D. ...
     * - Answer table: "Corrigé commenté" with pairs (1 C, 21 A, etc.)
     * - Explanations section: "Explications" with rows (1 C Explication...)
     */
    fun parse(rawText: String): List<QcmQuestion> {
        if (rawText.isBlank()) return emptyList()

        // 1. Clean line endings and filter out page numbers like "page 1" or "1 / 14"
        val lines = rawText.replace("\r", "").split("\n")
            .filter { line ->
                val tr = line.trim()
                !tr.matches(Regex("""^(?:page\s*)?\d{1,3}(?:\s*/\s*\d+)?$""", RegexOption.IGNORE_CASE))
            }

        val cleanFlatText = lines.joinToString(" ").replace(Regex("""\s+"""), " ")

        // 2. Identify Corrigé / Answers section (use last occurrence of Corrigé commenté)
        var corrigIndex = -1
        val corrigCommentMatches = Regex("""Corrig[ée]\s+comment[ée]""", RegexOption.IGNORE_CASE).findAll(cleanFlatText).toList()
        if (corrigCommentMatches.isNotEmpty()) {
            corrigIndex = corrigCommentMatches.last().range.first
        } else {
            val standaloneCorrig = Regex("""(?:^|\s)Corrig[ée](?:\s*[:\n]|\s+des|\s+N°)""", RegexOption.IGNORE_CASE).findAll(cleanFlatText).toList()
            if (standaloneCorrig.isNotEmpty()) {
                corrigIndex = standaloneCorrig.last().range.first
            } else {
                val repMatch = Regex("""N°\s*R[ée]p""", RegexOption.IGNORE_CASE).find(cleanFlatText)
                if (repMatch != null) {
                    corrigIndex = repMatch.range.first
                }
            }
        }
        if (corrigIndex < 0) {
            corrigIndex = cleanFlatText.length
        }

        val questionsPart = cleanFlatText.substring(0, corrigIndex)
        val keyPart = if (corrigIndex < cleanFlatText.length) cleanFlatText.substring(corrigIndex) else ""

        val explicationIndex = keyPart.indexOf("Explication", ignoreCase = true)
        val answersKeyPart = if (explicationIndex > 0) keyPart.substring(0, explicationIndex) else keyPart
        val explanationsPart = if (explicationIndex > 0) {
            keyPart.substring(explicationIndex)
                .replace(Regex("""N°\s*R[ée]p\.?\s*Explication""", RegexOption.IGNORE_CASE), " ")
        } else {
            ""
        }

        // 3. Extract answer key mapping: question number -> option letter
        val answerMap = mutableMapOf<Int, String>()
        val answerRegex = Regex("""(?:^|\s)(\d{1,3})\s+([A-E])(?=\s|$)""", RegexOption.IGNORE_CASE)
        for (match in answerRegex.findAll(answersKeyPart)) {
            val qNum = match.groupValues[1].toIntOrNull()
            val ansLetter = match.groupValues[2].uppercase()
            if (qNum != null && !answerMap.containsKey(qNum)) {
                answerMap[qNum] = ansLetter
            }
        }

        // 4. Split questions by "1. ", "2. ", etc.
        val qSplitPattern = Pattern.compile("""(?:^|\s)(\d{1,3})\.\s""")
        val matcher = qSplitPattern.matcher(questionsPart)
        val questionBounds = mutableListOf<Triple<Int, Int, Int>>() // (num, start, contentStart)

        while (matcher.find()) {
            val num = matcher.group(1)?.toIntOrNull() ?: continue
            val start = matcher.start()
            val contentStart = matcher.end()
            questionBounds.add(Triple(num, start, contentStart))
        }

        val questionList = mutableListOf<QcmQuestion>()

        for (i in questionBounds.indices) {
            val (num, _, contentStart) = questionBounds[i]
            val contentEnd = if (i + 1 < questionBounds.size) questionBounds[i + 1].second else questionsPart.length
            var block = questionsPart.substring(contentStart, contentEnd).trim()

            // Find options: A. B. C. D. E.
            val optionBounds = mutableListOf<Triple<String, Int, Int>>() // letter, start, contentStart
            var searchPos = 0
            val optionLetters = listOf("A", "B", "C", "D", "E")

            for (letter in optionLetters) {
                val optRegex = Regex("""(?:^|\s)$letter[\.\)]\s""")
                val optMatch = optRegex.find(block, searchPos)
                if (optMatch != null) {
                    optionBounds.add(Triple(letter, optMatch.range.first, optMatch.range.last + 1))
                    searchPos = optMatch.range.last + 1
                }
            }

            if (optionBounds.size < 2) {
                continue
            }

            var questionText = block.substring(0, optionBounds[0].second).trim()
            // Clean section headers if present at the top of question
            questionText = questionText.replace(Regex("""^Partie\s+\d+\s*[·•\-–:.].*?""", RegexOption.IGNORE_CASE), "").trim()

            var correctLetter = answerMap[num]

            val options = mutableListOf<QcmOption>()
            for (j in optionBounds.indices) {
                val optLetter = optionBounds[j].first
                val optStart = optionBounds[j].third
                val optEnd = if (j + 1 < optionBounds.size) optionBounds[j + 1].second else block.length
                var optText = block.substring(optStart, optEnd).trim()

                // If this is the last option of a block, it might have trailing section header e.g. "Partie 2 · La sonde"
                if (j == optionBounds.size - 1) {
                    val partMatch = Regex("""Partie\s+\d+\s*[·•\-–:.].*?$""", RegexOption.IGNORE_CASE).find(optText)
                    if (partMatch != null) {
                        optText = optText.substring(0, partMatch.range.first).trim()
                    }
                    val chapMatch = Regex("""Chapitre\s+[IVXLCDM\d]+\s*[·•\-–:.].*?$""", RegexOption.IGNORE_CASE).find(optText)
                    if (chapMatch != null) {
                        optText = optText.substring(0, chapMatch.range.first).trim()
                    }
                }

                // Check for inline asterisk or checkmark
                var isMarkedCorrect = false
                if (optText.startsWith("*") || optText.endsWith("*")) {
                    isMarkedCorrect = true
                    optText = optText.removePrefix("*").removeSuffix("*").trim()
                    if (correctLetter == null) correctLetter = optLetter
                }

                val isCorrect = (correctLetter != null && optLetter.equals(correctLetter, ignoreCase = true)) || isMarkedCorrect
                options.add(QcmOption(letter = optLetter, text = optText, isCorrect = isCorrect))
            }

            questionList.add(
                QcmQuestion(
                    number = num,
                    text = questionText,
                    options = options,
                    correctLetter = correctLetter,
                    explanation = ""
                )
            )
        }

        // 5. Match explanations
        if (explanationsPart.isNotBlank() && questionList.isNotEmpty()) {
            val explMap = mutableMapOf<Int, String>()
            val explMatcher = Pattern.compile("""(?:^|\s)(\d{1,3})\s+([A-E])\s+""")
            val matcherEx = explMatcher.matcher(explanationsPart)

            val explTokens = mutableListOf<Triple<Int, String, Pair<Int, Int>>>() // (num, letter, (start, end))
            while (matcherEx.find()) {
                val qNum = matcherEx.group(1)?.toIntOrNull() ?: continue
                val qLet = matcherEx.group(2) ?: ""
                val mStart = matcherEx.start()
                val mEnd = matcherEx.end()
                explTokens.add(Triple(qNum, qLet, Pair(mStart, mEnd)))
            }

            for (k in explTokens.indices) {
                val (qNum, _, bounds) = explTokens[k]
                val cStart = bounds.second
                val cEnd = if (k + 1 < explTokens.size) {
                    explTokens[k + 1].third.first
                } else {
                    explanationsPart.length
                }
                val explanationText = explanationsPart.substring(cStart, cEnd).trim()
                explMap[qNum] = explanationText
            }

            val updatedList = questionList.map { q ->
                val expl = explMap[q.number] ?: q.explanation
                q.copy(explanation = expl)
            }
            return updatedList
        }

        return questionList
    }
}
