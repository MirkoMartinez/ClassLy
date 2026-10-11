plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.classly"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.classly"
        minSdk = 24
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
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            pickFirsts.add("org/threeten/bp/**")
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

dependencies {
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1") {
        exclude(group = "com.jakewharton.threetenabp", module = "threetenabp")
    }
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.identity.credentials)
    implementation(libs.androidx.credentials.play.auth)
    implementation(libs.google.identity.lib)
    // Google ML Kit Text Recognition para OCR de PDFs escaneados
    implementation("com.google.mlkit:text-recognition:16.0.1")
    // Google Generative AI SDK para análisis multimodal con Gemini
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
