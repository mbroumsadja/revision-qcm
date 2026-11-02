package com.example.parser

import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream

object PdfTextExtractor {
    @Volatile
    private var isInitialized = false

    fun init(context: Context) {
        if (!isInitialized) {
            synchronized(this) {
                if (!isInitialized) {
                    PDFBoxResourceLoader.init(context.applicationContext)
                    isInitialized = true
                }
            }
        }
    }

    /**
     * Extracts full plain text from a PDF input stream using the PDFBox library.
     */
    fun extractText(context: Context, inputStream: InputStream): String {
        init(context)
        return try {
            val document = PDDocument.load(inputStream)
            document.use { doc ->
                val stripper = PDFTextStripper()
                stripper.sortByPosition = true
                stripper.getText(doc)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
