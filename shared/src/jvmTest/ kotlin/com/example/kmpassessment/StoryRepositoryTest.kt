package com.example.kmpassessment

import com.example.kmpassessment.network.StoryService
import com.example.kmpassessment.repository.StoryRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private const val FEED_JSON = """
    [
      {
        "headline": "Foo headline",
        "image_url": "https://example.com/img.jpg",
        "date_published": "2025-06-15"
      },
      {
        "headline": "Bar headline",
        "image_url": "https://example.com/img2.jpg",
        "date_published": "2025-06-14"
      }
    ]
"""

class StoryRepositoryTest {

    @Test fun happyPath() = runTest(StandardTestDispatcher()) {
        val repo = repoWith(json = FEED_JSON)
        repo.refresh()
        assertEquals(2, repo.stories.value.size)
    }

    @Test fun offlineFallback() = runTest(StandardTestDispatcher()) {
        val repo = repoWith(json = FEED_JSON)
        repo.refresh()                 // cache 2 rows

        // new repo with failing http
        val offlineRepo = repoWith(json = null, sameDb = repo)
        offlineRepo.refresh()
        assertEquals(2, offlineRepo.stories.value.size)   // pulled from cache
    }

    @Test fun filterByHeadline() = runTest(StandardTestDispatcher()) {
        val repo = repoWith(json = FEED_JSON)
        repo.refresh()
        val filtered = repo.filter("foo").value
        assertEquals(1, filtered.size)
        assertEquals("Foo headline", filtered.first().headline)
    }

    /* ---------- helpers ---------- */

    private fun repoWith(json: String?, sameDb: StoryRepository? = null): StoryRepository {
        val client = mockClient(json)
        val service = StoryService(client)
        val db = sameDb?.let { it }
            ?.let { null } ?: inMemoryDb()
        return StoryRepository(service, db)
    }
}
