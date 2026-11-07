package com.example.data.api

import com.example.model.AudiobookItem
import com.example.model.BookCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LibriVoxService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun searchAudiobooks(query: String? = null, limit: Int = 12): List<AudiobookItem> = withContext(Dispatchers.IO) {
        try {
            val url = if (!query.isNullOrBlank()) {
                "https://librivox.org/api/feed/audiobooks/?format=json&extended=1&limit=$limit&title=" +
                        java.net.URLEncoder.encode(query, "UTF-8")
            } else {
                "https://librivox.org/api/feed/audiobooks/?format=json&extended=1&limit=$limit"
            }

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext emptyList()

            val body = response.body?.string() ?: return@withContext emptyList()
            parseLibriVoxJson(body)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchPublicDomainClassics(): List<AudiobookItem> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://librivox.org/api/feed/audiobooks/?format=json&extended=1&limit=10")
                .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext emptyList()
            val body = response.body?.string() ?: return@withContext emptyList()
            parseLibriVoxJson(body)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseLibriVoxJson(jsonStr: String): List<AudiobookItem> {
        val list = mutableListOf<AudiobookItem>()
        val root = JSONObject(jsonStr)
        val booksArray = root.optJSONArray("books") ?: return emptyList()

        for (i in 0 until booksArray.length()) {
            val b = booksArray.optJSONObject(i) ?: continue
            val id = b.optString("id", "")
            val title = b.optString("title", "Untitled")
            val totalTime = b.optString("totaltime", "2h 45m")
            val archiveUrl = b.optString("url_iarchive", "")

            // Extract archive identifier for cover image
            val archiveId = if (archiveUrl.contains("/details/")) {
                archiveUrl.substringAfter("/details/").trimEnd('/')
            } else ""

            val cover = if (archiveId.isNotBlank()) {
                "https://archive.org/services/img/$archiveId"
            } else {
                ""
            }

            // Extract Authors
            var author = "Public Domain"
            val authorsArray = b.optJSONArray("authors")
            if (authorsArray != null && authorsArray.length() > 0) {
                val a = authorsArray.optJSONObject(0)
                if (a != null) {
                    val first = a.optString("first_name", "")
                    val last = a.optString("last_name", "")
                    author = "$first $last".trim()
                }
            }

            // Extract Sections / Chapter 1 stream URL
            var firstChapterListenUrl: String? = null
            var firstChapterTitle = "Chapter 1"
            var narrator = "LibriVox Volunteer"

            val sections = b.optJSONArray("sections")
            if (sections != null && sections.length() > 0) {
                val sec0 = sections.optJSONObject(0)
                if (sec0 != null) {
                    firstChapterListenUrl = sec0.optString("listen_url", "").replace("\\/", "/")
                    firstChapterTitle = sec0.optString("title", "Chapter 1")
                    val readers = sec0.optJSONArray("readers")
                    if (readers != null && readers.length() > 0) {
                        val r = readers.optJSONObject(0)
                        if (r != null) {
                            narrator = r.optString("display_name", narrator)
                        }
                    }
                }
            }

            list.add(
                AudiobookItem(
                    id = "librivox_$id",
                    title = title,
                    author = author,
                    narrator = narrator,
                    coverUrl = cover,
                    progressPercent = 0,
                    currentChapter = firstChapterTitle,
                    durationText = totalTime,
                    remainingText = totalTime,
                    rating = 4.8,
                    category = BookCategory.TRENDING,
                    isDownloaded = false,
                    isSynchronized = true,
                    speed = 1.0f,
                    formatTag = "LibriVox Free Audio",
                    audioStreamUrl = firstChapterListenUrl,
                    librivoxId = id
                )
            )
        }

        return list
    }
}
