package com.example.kmpassessment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Story(
    @SerialName("title") val headline: String,
    val media: Media? = null,
    @SerialName("publishedAtUnix") val published: Long
) {
    val imageUrl: String
        get() = media?.imageUrl.orEmpty()
}

@Serializable
data class Media(
    val imageUrl: String
)
