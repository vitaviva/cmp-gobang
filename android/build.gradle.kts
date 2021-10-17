plugins {
    id("org.jetbrains.compose") /*version "1.0.0-alpha2"*/
    id("com.android.application")
    kotlin("android")
}

group = "com.github.vitaviva"
version = "1.0"

repositories {
    google()
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.3.0-alpha03")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.github.vitaviva.android"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}