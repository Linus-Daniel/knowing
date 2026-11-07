package com.example

import com.example.data.SampleData
import com.example.model.BookCategory
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun verifySampleDataIntegrity() {
        assertNotNull(SampleData.KlaraAndTheSun)
        assertEquals("Klara and the Sun", SampleData.KlaraAndTheSun.title)
        assertEquals(68, SampleData.KlaraAndTheSun.progressPercent)
        assertTrue(SampleData.TrendingBooks.isNotEmpty())
        assertTrue(SampleData.ShortListens.isNotEmpty())
        assertTrue(SampleData.StaffPicks.isNotEmpty())
        assertTrue(SampleData.Chapter4Sentences.size >= 4)
        assertTrue(SampleData.VoiceOptions.any { it.id == "Nova" })
    }

    @Test
    fun verifyLibraryCategories() {
        val documents = SampleData.LibraryShelf.filter { it.category == BookCategory.MY_DOCUMENTS }
        assertTrue(documents.isNotEmpty())
        assertEquals("Deep_Work_Summary.pdf", documents.first().title)
    }
}
