import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.firebase.crashlytics.plugin)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

val googleClientId = localProperties.getProperty("GOOGLE_CLIENT_ID") ?: ""
val kakaoNativeAppKey = localProperties.getProperty("KAKAO_NATIVE_APP_KEY") ?: ""
val kakaoOauthHost = localProperties.getProperty("KAKAO_OAUTH_HOST") ?: ""
val naverClientId = localProperties.getProperty("NAVER_CLIEND_ID") ?: ""
val baseUrl = localProperties.getProperty("base_url") ?: ""
val websocketUrl = localProperties.getProperty("websocket_url") ?: ""

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

val releaseKeyAlias = keystoreProperties.getProperty("release.keyAlias") ?: ""
val releaseKeyPassword = keystoreProperties.getProperty("release.keyPassword") ?: ""
val releaseStoreFile = keystoreProperties.getProperty("release.storeFile") ?: ""
val releaseStorePassword = keystoreProperties.getProperty("release.storePassword") ?: ""

android {
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", kakaoNativeAppKey)
        resValue("string", "KAKAO_OAUTH_HOST", kakaoOauthHost)
        buildConfigField("String", "NAVER_CLIEND_ID", naverClientId)
        buildConfigField("String", "base_url", baseUrl)
        buildConfigField("String", "websocket_url", websocketUrl)
    }

    signingConfigs {
        create("release") {
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
            storeFile = file(releaseStoreFile)
            storePassword = releaseStorePassword
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.android)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.google)
    implementation(libs.bundles.kakao)
    implementation(libs.bundles.naver)
    implementation(libs.bundles.location)
    implementation(libs.bundles.network)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.animation)
    implementation(libs.bundles.stomp)
    implementation(libs.bundles.ballon)
    implementation(libs.bundles.room)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.assertj)
    androidTestImplementation(libs.androidx.espresso.core)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)
}
