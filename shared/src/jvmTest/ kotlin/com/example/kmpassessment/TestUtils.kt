package com.example.kmpassessment

import com.example.kmpassessment.cache.StoriesDatabase
import com.example.kmpassessment.model.Story
import com.example.kmpassessment.network.StoryService
import com.example.kmpassessment.repository.StoryRepository
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JvmSqliteDriver
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

fun inMemoryDb(): StoriesDatabase {
    val driver: SqlDriver = JvmSqliteDriver(StoriesDatabase.Schema)
    StoriesDatabase.Schema.create(driver)
    return StoriesDatabase(driver)
}

fun mockClient(json: String? = null): HttpClient =
    HttpClient(MockEngine) {
        install(ContentNegotiation) { json(Json) }
        engine {
            addHandler { _ ->
                if (json != null)
                    respond(json, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
                else
                    respondError(HttpStatusCode.InternalServerError)
            }
        }
    }
