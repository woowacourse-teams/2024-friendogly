package com.happy.friendogly.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.happy.friendogly.BuildConfig
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.local.room.ChatRoomDao
import com.happy.friendogly.remote.api.AuthService
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import javax.inject.Inject

@HiltAndroidApp
class FriendoglyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIEND_ID)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        AppModule.setInstance(applicationContext)
        FirebaseApp.initializeApp(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
