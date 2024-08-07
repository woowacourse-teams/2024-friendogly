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

android {
    namespace = "com.happy.friendogly"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.happy.friendogly"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "0.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", kakaoNativeAppKey)
        resValue("string", "KAKAO_OAUTH_HOST", kakaoOauthHost)
        buildConfigField("String", "NAVER_CLIEND_ID", naverClientId)
        buildConfigField("String", "base_url", baseUrl)
        buildConfigField("String", "websocket_url", websocketUrl)
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
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
    dataBinding {
        enable = true
    }
    viewBinding {
        enable = true
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
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
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
