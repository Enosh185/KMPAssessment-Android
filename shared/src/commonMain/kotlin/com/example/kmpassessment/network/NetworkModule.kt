package com.example.kmpassessment.network

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkModule {
    private val json = Json { ignoreUnknownKeys = true }
    fun client(engine: HttpClientEngine) = HttpClient(engine) {
        install(ContentNegotiation) { json(json) }
    }
}
