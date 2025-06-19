package com.example.kmpassessment.network

import com.example.kmpassessment.Story
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryEnvelope(
    @SerialName("stories") val stories: List<Story>
)
