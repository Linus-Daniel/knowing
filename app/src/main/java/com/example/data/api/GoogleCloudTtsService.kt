package com.example.data.api

import android.content.Context
import android.util.Base64
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class GoogleCloudTtsService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GOOGLE_CLOUD_TTS_API_KEY
        return !key.isNullOrBlank() &&
                key != "YOUR_GOOGLE_CLOUD_TTS_API_KEY" &&
                !key.startsWith("YOUR_")
    }

    suspend fun synthesizeSpeech(
        text: String,
        voiceName: String = "en-US-Neural2-F",
        languageCode: String = "en-US",
        speakingRate: Float = 1.0f,
        pitch: Float = 0.0f
    ): Result<File> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GOOGLE_CLOUD_TTS_API_KEY
        if (!isApiKeyConfigured()) {
            return@withContext Result.failure(IllegalStateException("Google Cloud TTS API key is not configured."))
        }

        try {
            val url = "https://texttospeech.googleapis.com/v1/text:synthesize?key=$apiKey"

            val jsonBody = JSONObject().apply {
                put("input", JSONObject().put("text", text))
                put("voice", JSONObject().apply {
                    put("languageCode", languageCode)
                    put("name", voiceName)
                })
                put("audioConfig", JSONObject().apply {
                    put("audioEncoding", "MP3")
                    put("speakingRate", speakingRate.toDouble())
                    put("pitch", pitch.toDouble())
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errorBody = response.body?.string().orEmpty()
                return@withContext Result.failure(
                    RuntimeException("Google Cloud TTS failed (${response.code}): $errorBody")
                )
            }

            val responseBody = response.body?.string()
                ?: return@withContext Result.failure(RuntimeException("Empty response from Google Cloud TTS"))

            val jsonResponse = JSONObject(responseBody)
            val audioContentBase64 = jsonResponse.optString("audioContent")
            if (audioContentBase64.isBlank()) {
                return@withContext Result.failure(RuntimeException("No audioContent in Google Cloud TTS response"))
            }

            val audioBytes = Base64.decode(audioContentBase64, Base64.DEFAULT)
            val outputFile = File(context.cacheDir, "gcloud_tts_${System.currentTimeMillis()}.mp3")
            FileOutputStream(outputFile).use { it.write(audioBytes) }

            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
