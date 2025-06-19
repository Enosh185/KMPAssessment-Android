package com.example.kmpassessment

import com.example.kmpassessment.network.StoryService
import com.example.kmpassessment.Story
import io.ktor.client.*

class DummyHttpClient : HttpClient()

class StoryServiceTestHelper : StoryService(client = DummyHttpClient()) {
    fun decodeForTest(json: String): List<Story> {
        val method = StoryService::class.java.getDeclaredMethod("decode", String::class.java)
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return method.invoke(this, json) as List<Story>
    }
}
