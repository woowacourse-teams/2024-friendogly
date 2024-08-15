package com.happy.friendogly.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.happy.friendogly.BuildConfig
import com.happy.friendogly.application.di.AppModule
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk

class FriendoglyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIEND_ID)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        AppModule.setInstance(applicationContext)
        FirebaseApp.initializeApp(this)
    }
}
