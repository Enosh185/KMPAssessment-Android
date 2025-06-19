package com.example.kmpassessment.repository

import com.example.kmpassessment.cache.StoriesDatabase
import com.example.kmpassessment.cache.StoryQueries
import com.example.kmpassessment.Story
import com.example.kmpassessment.network.StoryService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class StoryRepository(
    private val api: StoryService,
    db: StoriesDatabase,
    private val io: CoroutineDispatcher = Dispatchers.Default
) {
    private val queries: StoryQueries = db.storyQueries
    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    suspend fun refresh(): Result<List<Story>> = withContext(io) {
        // Step 1: Emit cached stories first for fast UI response
        _stories.value = queries.selectAll()
            .executeAsList()
            .map { Story(it.headline, com.example.kmpassessment.Media(it.imageUrl), it.published.toLong()) }

        // Step 2: Try refreshing from network
        runCatching { api.fetchStories() }
            .onSuccess { list ->
                queries.transaction {
                    queries.deleteAll()
                    list.forEach { queries.insertStory(it) }
                }
                _stories.value = list
                println("StoryRepository: fetched ${list.size} stories")
            }
            .onFailure { t ->
                println("StoryRepository: refresh FAILED (${t.javaClass.simpleName})")
            }
    }


    fun filter(q: String): Flow<List<Story>> =
        stories.map { list ->
            if (q.isBlank()) list
            else list.filter { it.headline.contains(q, ignoreCase = true) }
        }

    private fun StoryQueries.insertStory(s: Story) =
        insertStory(s.headline, s.media?.imageUrl.orEmpty(), s.published)
}
