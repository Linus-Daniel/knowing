package com.example.model

enum class BookCategory {
    CONTINUE_READING,
    TRENDING,
    SHORT_LISTENS,
    STAFF_PICKS,
    MY_DOCUMENTS
}

enum class TtsEngineType {
    GOOGLE_CLOUD_NEURAL,
    ANDROID_NATIVE_FALLBACK
}

data class AudiobookItem(
    val id: String,
    val title: String,
    val author: String,
    val narrator: String,
    val coverUrl: String,
    val progressPercent: Int = 0,
    val currentChapter: String = "Chapter 1",
    val durationText: String = "8h 12m",
    val remainingText: String = "4h 10m",
    val rating: Double = 4.8,
    val category: BookCategory = BookCategory.TRENDING,
    val isDownloaded: Boolean = false,
    val isSynchronized: Boolean = true,
    val speed: Float = 1.0f,
    val formatTag: String = "Listen & Read",
    val audioStreamUrl: String? = null,
    val librivoxId: String? = null
)

data class SentenceItem(
    val id: Int,
    val text: String,
    val startTimeSeconds: Float,
    val endTimeSeconds: Float,
    val isRead: Boolean = false,
    val userNote: String? = null
)

data class VoiceModel(
    val id: String,
    val name: String,
    val badge: String,
    val description: String,
    val sampleText: String,
    val googleCloudVoiceName: String = "en-US-Neural2-F"
)

data class UserHighlight(
    val id: String,
    val bookTitle: String,
    val chapter: String,
    val quote: String,
    val marginaliaNote: String,
    val timestamp: String,
    val dateText: String
)

data class DocumentImportState(
    val fileName: String = "Neuromancer_Manuscript_Ch1-3.epub",
    val fileSize: String = "4.2 MB",
    val wordCount: Int = 14820,
    val chaptersCount: Int = 3,
    val stage1Complete: Boolean = true,
    val stage2Complete: Boolean = true,
    val stage3Progress: Float = 0.74f,
    val alignedSentences: Int = 382,
    val totalSentences: Int = 516,
    val selectedVoiceId: String = "Nova",
    val speed: Float = 1.25f,
    val pitchShift: Float = 0f,
    val paragraphPauseSeconds: Float = 0.5f,
    val targetDialect: String = "English - US (Neural v4 Ultra)"
)
