// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.google.services)
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.firebase.crashlytics.plugin) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.hilt) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
