plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mundonoobs.pcremoteapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.pcremoteapp"
        minSdk = 35
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // WebSockets
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Serialización JSON rápida
    implementation("com.google.code.gson:gson:2.10.1")
    // Cliente WebSocket (OkHttp)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Serializador JSON
    implementation("com.google.code.gson:gson:2.10.1")

}
