enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.10.1"
        id("com.android.library")     version "8.10.1"
        // Kotlin plugins (already on classpath, but pin for clarity)
        kotlin("multiplatform")       version "2.0.0"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KMP_Assessment"
include(":androidApp")
include(":shared")