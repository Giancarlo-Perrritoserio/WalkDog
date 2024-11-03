// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false //firebase
    id("com.google.dagger.hilt.android") version "2.49" apply false // Hilt
    id("com.android.library") version "8.5.1" apply false
}