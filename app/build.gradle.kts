plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services") //fire base
    kotlin("kapt") // Agrega esta línea
    id("com.google.dagger.hilt.android") // Hilt
}

android {
    namespace = "com.proyecto.WalkDog"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.proyecto.WalkDog"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val mapsComposeVersion = "4.4.1"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.auth.ktx) // Autenticación Firebase

    // Dependencias necesarias según algunos casos
    implementation(libs.material)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Navegación de Hilt en Compose
    implementation(libs.androidx.hilt.navigation.compose)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Google Maps Compose
    implementation(libs.maps.compose)
    // Utilidades de Google Maps para Jetpack Compose
    implementation(libs.maps.compose.utils)
    // Widgets de Google Maps Compose
    implementation(libs.maps.compose.widgets)
    // Google Play Services Location
    implementation(libs.play.services.location)
    // UI Dependencies
    implementation(libs.material)
    implementation(libs.ui.tooling.preview)
    implementation(libs.maps.compose.v220)

    //dependencias agregadas en parte 3.0
    implementation(libs.firebase.firestore) // Asegúrate de tener la dependencia de Firebase

    //dependencias parte 3.1
    implementation(libs.play.services.maps)
}

