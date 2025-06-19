package com.example.kmpassessment

import com.example.kmpassessment.StoryServiceTestHelper
import com.example.kmpassessment.network.StoryService
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StoryServiceTest {

    private val service = StoryServiceTestHelper()

    @Test
    fun `parses valid story JSON correctly`() = runTest {
        val json = """
            {
              "data": [
                {
                  "title": "Test Headline",
                  "publishedAtUnix": 1732629001317,
                  "media": {
                    "imageUrl": "https://example.com/test.jpg"
                  }
                }
              ]
            }
        """.trimIndent()

        val stories = service.decodeForTest(json)

        assertEquals(1, stories.size)
        assertEquals("Test Headline", stories.first().headline)
        assertEquals("https://example.com/test.jpg", stories.first().imageUrl)
        assertTrue(stories.first().published > 0)
    }

    @Test
    fun `skips malformed story entries gracefully`() = runTest {
        val json = """
            {
              "data": [
                { "foo": "bar" },
                {
                  "title": "Valid",
                  "publishedAtUnix": 1732629001317,
                  "media": { "imageUrl": "https://example.com/image.jpg" }
                }
              ]
            }
        """.trimIndent()

        val stories = service.decodeForTest(json)

        assertEquals(1, stories.size)
        assertEquals("Valid", stories.first().headline)
    }

    @Test
    fun `returns empty list for empty dataset`() = runTest {
        val json = """{ "data": [] }"""
        val stories = service.decodeForTest(json)
        assertEquals(0, stories.size)
    }
}
