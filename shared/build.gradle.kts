import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")                    // 2.0.0 from root classpath
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.android.library")
    id("app.cash.sqldelight") version "2.0.0"  // v2 plugin & Maven group
}

kotlin {

    jvm()
    /** ------------ Android ------------ **/
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions    { jvmTarget.set(JvmTarget.JVM_17) }
            }
        }
    }

    /** ------------ iOS binaries ------------ **/
    val iosX64     = iosX64()
    val iosArm64   = iosArm64()
    val iosSimulatorArm64 = iosSimulatorArm64()

    listOf(iosX64, iosArm64, iosSimulatorArm64).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    /** ------------ Source sets ------------ **/
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

                implementation("app.cash.sqldelight:runtime:2.0.0")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
            }
        }
        val commonTest by getting { dependencies { implementation(kotlin("test")) } }

        /* ---- Android ---- */
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.3.5")
                implementation("app.cash.sqldelight:android-driver:2.0.0")
            }
        }

        /* ---- iOS aggregate ---- */
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.5")
                implementation("app.cash.sqldelight:native-driver:2.0.0")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
                implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
                implementation("io.ktor:ktor-client-mock:2.3.5")
            }
        }
    }
}

android {
    namespace = "com.example.kmpassessment"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

/* ------------ SQLDelight v2 ------------ */
sqldelight {
    databases {
        create("StoriesDatabase") {
            packageName.set("com.example.kmpassessment.cache")
        }
    }
}
