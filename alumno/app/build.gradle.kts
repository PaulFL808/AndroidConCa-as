plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.asistenciaalumno"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.asistenciaalumno"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // 1. Forzamos las versiones estables y eliminamos el "libs.androidx.core.ktx"
    implementation("androidx.core:core:1.13.1")
    implementation("androidx.core:core-ktx:1.13.1")

    // 2. Librerías visuales estándar de Android
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 3. Firebase (Exactamente la versión que pidió tu compañero, sin el BoM)
    implementation("com.google.firebase:firebase-database:20.3.1")

    // 4. Corrutinas y ViewModel para que no se congele la pantalla
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
}