package com.example.kmpassessment

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform