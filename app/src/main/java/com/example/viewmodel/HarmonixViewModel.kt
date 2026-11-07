package com.example.viewmodel

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.data.api.GoogleCloudTtsService
import com.example.data.api.LibriVoxService
import com.example.model.AudiobookItem
import com.example.model.DocumentImportState
import com.example.model.SentenceItem
import com.example.model.TtsEngineType
import com.example.model.UserHighlight
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

enum class ScreenDestination {
    HOME,
    STUDIO,      // Document Import
    READER,      // Synchronized Reader & Audio Player
    LIBRARY,
    PROFILE
}

data class HarmonixUiState(
    val currentScreen: ScreenDestination = ScreenDestination.HOME,
    val previousScreen: ScreenDestination = ScreenDestination.HOME,
    val activeBook: AudiobookItem = SampleData.KlaraAndTheSun,
    val isPlaying: Boolean = false,
    val playbackSeconds: Float = 14.22f,
    val chapterTotalSeconds: Float = 36.37f,
    val activeSentenceIndex: Int = 2,
    val sentences: List<SentenceItem> = SampleData.Chapter4Sentences,
    val themeMode: AppThemeMode = AppThemeMode.LIGHT,
    val typefaceChoice: String = "Literata",
    val fontSizeSp: Int = 20,
    val playbackSpeed: Float = 1.25f,
    val sleepTimerMinutes: Int? = 30,
    val isBookmarked: Boolean = true,
    val isTypographySheetOpen: Boolean = false,
    val documentImportState: DocumentImportState = DocumentImportState(),
    val isSynthesizing: Boolean = false,
    val librarySegmentIndex: Int = 0, // 0: All, 1: Audiobooks, 2: Documents, 3: Downloaded
    val libraryFilterChipIndex: Int = 0, // 0: Reading, 1: Want, 2: Finished, 3: Quotes
    val librarySearchQuery: String = "",
    val isQuoteModalOpen: Boolean = false,
    val quoteModalPaletteIndex: Int = 0,
    val activeHighlight: UserHighlight = SampleData.FeaturedHighlight,
    val userQuoteExportedToast: Boolean = false,
    val userBookmarkToast: String? = null,
    val playingVoiceSampleId: String? = null,
    // Live API integration states
    val ttsEngine: TtsEngineType = TtsEngineType.ANDROID_NATIVE_FALLBACK,
    val ttsEngineStatusText: String = "Android Native TTS (Offline)",
    val isGoogleCloudConfigured: Boolean = false,
    val librivoxBooks: List<AudiobookItem> = emptyList(),
    val isLibriVoxLoading: Boolean = false,
    val audioStreamingError: String? = null
)

class HarmonixViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val _uiState = MutableStateFlow(HarmonixUiState())
    val uiState: StateFlow<HarmonixUiState> = _uiState.asStateFlow()

    private var androidTts: TextToSpeech? = null
    private var isAndroidTtsReady: Boolean = false
    private var mediaPlayer: MediaPlayer? = null
    private var playbackLoopJob: Job? = null

    private val googleCloudTtsService = GoogleCloudTtsService(application)
    private val libriVoxService = LibriVoxService()

    init {
        androidTts = TextToSpeech(application, this)
        val isCloudKeySet = googleCloudTtsService.isApiKeyConfigured()
        _uiState.update {
            it.copy(
                isGoogleCloudConfigured = isCloudKeySet,
                ttsEngine = if (isCloudKeySet) TtsEngineType.GOOGLE_CLOUD_NEURAL else TtsEngineType.ANDROID_NATIVE_FALLBACK,
                ttsEngineStatusText = if (isCloudKeySet) "Google Cloud Neural2" else "Android Native TTS (Fallback Active)"
            )
        }

        loadLibriVoxAudiobooks()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            androidTts?.language = Locale.US
            isAndroidTtsReady = true
        }
    }

    fun loadLibriVoxAudiobooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLibriVoxLoading = true) }
            val books = libriVoxService.fetchPublicDomainClassics()
            if (books.isNotEmpty()) {
                _uiState.update { it.copy(librivoxBooks = books, isLibriVoxLoading = false) }
            } else {
                _uiState.update { it.copy(isLibriVoxLoading = false) }
            }
        }
    }

    fun searchLibriVox(query: String) {
        _uiState.update { it.copy(librarySearchQuery = query) }
        if (query.length >= 3) {
            viewModelScope.launch {
                val results = libriVoxService.searchAudiobooks(query)
                if (results.isNotEmpty()) {
                    _uiState.update { it.copy(librivoxBooks = results) }
                }
            }
        }
    }

    fun navigateTo(screen: ScreenDestination) {
        _uiState.update { current ->
            current.copy(
                previousScreen = current.currentScreen,
                currentScreen = screen
            )
        }
    }

    fun navigateBack(): Boolean {
        val current = _uiState.value
        if (current.currentScreen != ScreenDestination.HOME) {
            _uiState.update { it.copy(currentScreen = ScreenDestination.HOME) }
            return true
        }
        return false
    }

    fun selectBookToPlay(book: AudiobookItem, startReader: Boolean = true) {
        stopAllAudio()
        _uiState.update {
            it.copy(
                activeBook = book,
                isPlaying = true,
                currentScreen = if (startReader) ScreenDestination.READER else it.currentScreen,
                playbackSeconds = 0f
            )
        }

        // If this book has a real streaming audio URL (e.g. from LibriVox), stream it!
        if (!book.audioStreamUrl.isNullOrBlank()) {
            streamAudioUrl(book.audioStreamUrl)
        } else {
            // Read first sentence with Google Cloud TTS (or Android fallback)
            startPlaybackSimulation()
            speakCurrentSentence()
        }
    }

    private fun streamAudioUrl(url: String) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { mp ->
                    mp.start()
                    val totalSec = (mp.duration / 1000f).coerceAtLeast(60f)
                    _uiState.update {
                        it.copy(
                            isPlaying = true,
                            chapterTotalSeconds = totalSec
                        )
                    }
                    startStreamProgressTracking()
                }
                setOnCompletionListener {
                    _uiState.update { it.copy(isPlaying = false) }
                }
                setOnErrorListener { _, _, _ ->
                    // Fall back to synthetic speech simulation if network stream fails
                    _uiState.update { it.copy(audioStreamingError = "Network audio stream failed. Using reader audio.") }
                    startPlaybackSimulation()
                    speakCurrentSentence()
                    true
                }
            }
        } catch (e: Exception) {
            startPlaybackSimulation()
            speakCurrentSentence()
        }
    }

    private fun startStreamProgressTracking() {
        playbackLoopJob?.cancel()
        playbackLoopJob = viewModelScope.launch {
            while (_uiState.value.isPlaying) {
                delay(1000)
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        val currentSec = mp.currentPosition / 1000f
                        val sentences = _uiState.value.sentences
                        val newIdx = sentences.indexOfFirst { it.endTimeSeconds >= currentSec }
                            .takeIf { it >= 0 } ?: _uiState.value.activeSentenceIndex

                        _uiState.update {
                            it.copy(
                                playbackSeconds = currentSec,
                                activeSentenceIndex = newIdx
                            )
                        }
                    }
                }
            }
        }
    }

    fun togglePlayback() {
        val newPlaying = !_uiState.value.isPlaying
        _uiState.update { it.copy(isPlaying = newPlaying) }

        if (newPlaying) {
            if (mediaPlayer != null) {
                try {
                    mediaPlayer?.start()
                    startStreamProgressTracking()
                } catch (e: Exception) {
                    speakCurrentSentence()
                    startPlaybackSimulation()
                }
            } else {
                startPlaybackSimulation()
                speakCurrentSentence()
            }
        } else {
            mediaPlayer?.pause()
            playbackLoopJob?.cancel()
            androidTts?.stop()
        }
    }

    fun seekToSentence(index: Int) {
        val sentences = _uiState.value.sentences
        if (index in sentences.indices) {
            val sentence = sentences[index]
            _uiState.update {
                it.copy(
                    activeSentenceIndex = index,
                    playbackSeconds = sentence.startTimeSeconds,
                    isPlaying = true
                )
            }

            if (mediaPlayer != null) {
                try {
                    mediaPlayer?.seekTo((sentence.startTimeSeconds * 1000).toInt())
                } catch (_: Exception) {}
            }

            speakTextWithFallback(sentence.text)
            startPlaybackSimulation()
        }
    }

    fun skip15Seconds(forward: Boolean) {
        _uiState.update { current ->
            val delta = if (forward) 15f else -15f
            val newTime = (current.playbackSeconds + delta).coerceIn(0f, current.chapterTotalSeconds * 60f)
            val newIdx = current.sentences.indexOfFirst { it.endTimeSeconds >= newTime }.coerceAtLeast(0)

            mediaPlayer?.let { mp ->
                try {
                    mp.seekTo((newTime * 1000).toInt())
                } catch (_: Exception) {}
            }

            current.copy(
                playbackSeconds = newTime,
                activeSentenceIndex = newIdx
            )
        }
    }

    fun cycleSpeed() {
        val speeds = listOf(1.0f, 1.25f, 1.5f, 1.75f, 2.0f)
        val current = _uiState.value.playbackSpeed
        val nextIdx = (speeds.indexOf(current) + 1) % speeds.size
        val newSpeed = speeds[nextIdx]
        _uiState.update { it.copy(playbackSpeed = newSpeed) }

        androidTts?.setSpeechRate(newSpeed)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        it.playbackParams = it.playbackParams.setSpeed(newSpeed)
                    }
                }
            } catch (_: Exception) {}
        }
    }

    fun toggleBookmark() {
        _uiState.update { current ->
            val next = !current.isBookmarked
            current.copy(
                isBookmarked = next,
                userBookmarkToast = if (next) "Bookmark saved at ${formatTimestamp(current.playbackSeconds)}" else "Bookmark removed"
            )
        }
    }

    fun clearBookmarkToast() {
        _uiState.update { it.copy(userBookmarkToast = null) }
    }

    fun setThemeMode(mode: AppThemeMode) {
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun setTypefaceChoice(typeface: String) {
        _uiState.update { it.copy(typefaceChoice = typeface) }
    }

    fun adjustFontSize(increment: Boolean) {
        _uiState.update { current ->
            val delta = if (increment) 1 else -1
            val newSize = (current.fontSizeSp + delta).coerceIn(14, 26)
            current.copy(fontSizeSp = newSize)
        }
    }

    fun toggleTypographySheet(open: Boolean) {
        _uiState.update { it.copy(isTypographySheetOpen = open) }
    }

    // Document Import Actions
    fun updateTargetDialect(dialect: String) {
        _uiState.update {
            it.copy(documentImportState = it.documentImportState.copy(targetDialect = dialect))
        }
    }

    fun selectVoice(voiceId: String) {
        _uiState.update {
            it.copy(documentImportState = it.documentImportState.copy(selectedVoiceId = voiceId))
        }
    }

    fun playVoiceSample(voice: com.example.model.VoiceModel) {
        if (_uiState.value.playingVoiceSampleId == voice.id) {
            stopAllAudio()
            _uiState.update { it.copy(playingVoiceSampleId = null) }
        } else {
            _uiState.update { it.copy(playingVoiceSampleId = voice.id) }
            speakTextWithFallback(
                text = voice.sampleText,
                googleCloudVoiceName = voice.googleCloudVoiceName,
                onComplete = {
                    if (_uiState.value.playingVoiceSampleId == voice.id) {
                        _uiState.update { it.copy(playingVoiceSampleId = null) }
                    }
                }
            )
        }
    }

    fun updateImportSpeed(speed: Float) {
        _uiState.update {
            it.copy(documentImportState = it.documentImportState.copy(speed = speed))
        }
    }

    fun updatePitchShift(pitch: Float) {
        _uiState.update {
            it.copy(documentImportState = it.documentImportState.copy(pitchShift = pitch))
        }
    }

    fun updateParagraphPause(pause: Float) {
        _uiState.update {
            it.copy(documentImportState = it.documentImportState.copy(paragraphPauseSeconds = pause))
        }
    }

    fun triggerSynthesize() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSynthesizing = true) }
            for (step in 75..100 step 5) {
                delay(180)
                _uiState.update {
                    it.copy(
                        documentImportState = it.documentImportState.copy(
                            stage3Progress = step / 100f,
                            alignedSentences = (516 * (step / 100f)).toInt()
                        )
                    )
                }
            }
            delay(250)
            _uiState.update {
                it.copy(
                    isSynthesizing = false,
                    currentScreen = ScreenDestination.READER
                )
            }
            speakCurrentSentence()
        }
    }

    // Library Actions
    fun setLibrarySearchQuery(query: String) {
        searchLibriVox(query)
    }

    val isGoogleCloudTtsConfigured: Boolean
        get() = googleCloudTtsService.isApiKeyConfigured()

    fun setLibrarySegment(index: Int) {
        _uiState.update { it.copy(librarySegmentIndex = index) }
    }

    fun setLibraryFilterChip(index: Int) {
        _uiState.update { it.copy(libraryFilterChipIndex = index) }
    }

    fun openQuoteModal(highlight: UserHighlight) {
        _uiState.update { it.copy(isQuoteModalOpen = true, activeHighlight = highlight) }
    }

    fun closeQuoteModal() {
        _uiState.update { it.copy(isQuoteModalOpen = false, userQuoteExportedToast = false) }
    }

    fun setQuoteModalPalette(index: Int) {
        _uiState.update { it.copy(quoteModalPaletteIndex = index) }
    }

    fun shareQuoteCard() {
        viewModelScope.launch {
            _uiState.update { it.copy(userQuoteExportedToast = true) }
            delay(1200)
            _uiState.update { it.copy(isQuoteModalOpen = false, userQuoteExportedToast = false) }
        }
    }

    private fun startPlaybackSimulation() {
        playbackLoopJob?.cancel()
        playbackLoopJob = viewModelScope.launch {
            while (_uiState.value.isPlaying) {
                delay(1000)
                _uiState.update { current ->
                    val nextTime = current.playbackSeconds + 1f
                    val sentences = current.sentences
                    val currentSentence = sentences.getOrNull(current.activeSentenceIndex)
                    val nextIndex = if (currentSentence != null && nextTime > currentSentence.endTimeSeconds) {
                        val n = (current.activeSentenceIndex + 1).coerceAtMost(sentences.lastIndex)
                        if (n != current.activeSentenceIndex) {
                            speakCurrentSentence()
                        }
                        n
                    } else {
                        current.activeSentenceIndex
                    }
                    current.copy(
                        playbackSeconds = nextTime,
                        activeSentenceIndex = nextIndex
                    )
                }
            }
        }
    }

    private fun speakCurrentSentence() {
        val current = _uiState.value
        val sentence = current.sentences.getOrNull(current.activeSentenceIndex)
        if (sentence != null) {
            val selectedVoice = SampleData.VoiceOptions.find { it.id == current.documentImportState.selectedVoiceId }
            speakTextWithFallback(
                text = sentence.text,
                googleCloudVoiceName = selectedVoice?.googleCloudVoiceName ?: "en-US-Neural2-F"
            )
        }
    }

    /**
     * Primary speech synthesis engine:
     * Tries Google Cloud Text-to-Speech API first.
     * If the API key is not configured, or if the network request fails,
     * seamlessly and immediately falls back to Android Native TextToSpeech.
     */
    fun speakTextWithFallback(
        text: String,
        googleCloudVoiceName: String = "en-US-Neural2-F",
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            if (googleCloudTtsService.isApiKeyConfigured()) {
                val result = googleCloudTtsService.synthesizeSpeech(
                    text = text,
                    voiceName = googleCloudVoiceName,
                    speakingRate = _uiState.value.playbackSpeed
                )

                if (result.isSuccess) {
                    val file = result.getOrNull()
                    if (file != null && file.exists()) {
                        playMp3File(file, onComplete)
                        _uiState.update {
                            it.copy(
                                ttsEngine = TtsEngineType.GOOGLE_CLOUD_NEURAL,
                                ttsEngineStatusText = "Google Cloud Neural2 (Active)"
                            )
                        }
                        return@launch
                    }
                }
            }

            // Fallback to Android Native TextToSpeech
            _uiState.update {
                it.copy(
                    ttsEngine = TtsEngineType.ANDROID_NATIVE_FALLBACK,
                    ttsEngineStatusText = "Android Native TTS (Fallback Active)"
                )
            }
            speakViaAndroidTts(text, onComplete)
        }
    }

    private fun playMp3File(file: File, onComplete: (() -> Unit)?) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
                setOnCompletionListener {
                    onComplete?.invoke()
                }
                setOnErrorListener { _, _, _ ->
                    onComplete?.invoke()
                    true
                }
            }
        } catch (e: Exception) {
            onComplete?.invoke()
        }
    }

    private fun speakViaAndroidTts(text: String, onComplete: (() -> Unit)?) {
        if (isAndroidTtsReady) {
            androidTts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "harmonix_speech")
        }
        if (onComplete != null) {
            viewModelScope.launch {
                delay(3000)
                onComplete.invoke()
            }
        }
    }

    private fun stopAllAudio() {
        playbackLoopJob?.cancel()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (_: Exception) {}
        androidTts?.stop()
    }

    fun formatTimestamp(totalSeconds: Float): String {
        val mins = (totalSeconds / 60).toInt()
        val secs = (totalSeconds % 60).toInt()
        return "%02d:%02d".format(mins, secs)
    }

    override fun onCleared() {
        super.onCleared()
        stopAllAudio()
        androidTts?.shutdown()
    }
}
