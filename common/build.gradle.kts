import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") /*version "1.0.0-alpha2"*/
    id("com.android.library")
    id("kotlin-android-extensions")
}

group = "com.github.vitaviva"
version = "1.0"

repositories {
    google()
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            val RSocketVersion = "0.13.1"

            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

                //RSocket & Ktor
                implementation("io.rsocket.kotlin:rsocket-core:$RSocketVersion")
                implementation("io.rsocket.kotlin:rsocket-transport-ktor:$RSocketVersion") // TCP ktor transport
                implementation("io.rsocket.kotlin:rsocket-transport-ktor-client:$RSocketVersion")// WS ktor transport client plugin
                implementation("io.rsocket.kotlin:rsocket-transport-ktor-server:$RSocketVersion")// WS ktor transport server plugin
                implementation("io.ktor:ktor-client-cio:1.6.2")
                implementation("io.ktor:ktor-server-cio:1.6.2")
            }
        }
        val commonTest by getting {
            dependencies {
//                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.3.0")
                api("androidx.core:core-ktx:1.3.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }
        val desktopMain by getting
        val desktopTest by getting
    }
}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}