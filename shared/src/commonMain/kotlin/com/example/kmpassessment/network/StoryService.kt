package com.example.kmpassessment.network

import android.util.Log
import com.example.kmpassessment.Story
import com.example.kmpassessment.Media // ✅ this was missing
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.*

private val json = Json {
    ignoreUnknownKeys = true
    isLenient        = true
}

private fun String.preview(max: Int = 350): String =
    replace('\n', ' ').let { if (it.length <= max) it else it.take(max) + " …" }

private fun JsonObject.stringOrNull(vararg keys: String): String? =
    keys.firstNotNullOfOrNull { k -> this[k]?.jsonPrimitive?.contentOrNull }

class StoryService(private val client: HttpClient) {

    suspend fun fetchStories(): List<Story> {
        val raw = client
            .get("https://cbcmusic.github.io/assessment-tmp/data/data.json")
            .bodyAsText()

        Log.i("StoryService", "RAW ↓ ${raw.preview()}")

        return runCatching { decode(raw) }
            .onSuccess { Log.i("StoryService", "Decoded ${it.size} stories") }
            .onFailure { Log.e("StoryService", "Decoding failed", it) }
            .getOrDefault(emptyList())
    }

    private fun decode(text: String): List<Story> {
        val root = json.parseToJsonElement(text)

        val array: JsonArray = when (root) {
            is JsonArray  -> root
            is JsonObject -> (root["stories"] ?: root["data"])?.jsonArray ?: JsonArray(emptyList())
            else          -> JsonArray(emptyList())
        }

        return array.mapNotNull { element ->
            runCatching { json.decodeFromJsonElement<Story>(element) }
                .getOrElse {
                    Log.w("StoryService", "Manual mapping triggered")
                    manualMap(element)
                }
        }
    }

    private fun manualMap(el: JsonElement): Story? = try {
        val obj   = el.jsonObject
        val asset = obj["asset"]?.jsonObject

        val headline = obj.stringOrNull("headline", "title")
            ?: asset?.stringOrNull("headline", "title")
            ?: return null

        val imageUrl = obj.stringOrNull("image_url", "imageUrl")
            ?: asset?.stringOrNull("image_url", "imageUrl")
            ?: ""

        val published = obj.stringOrNull("date_published", "published", "datePublished", "publishedAtUnix")
            ?: asset?.stringOrNull("date_published", "published", "datePublished", "publishedAtUnix")
            ?: "0"

        Story(
            headline = headline,
            media = Media(imageUrl),
            published = published.toLongOrNull() ?: 0L
        )
    } catch (t: Throwable) {
        null
    }
}
