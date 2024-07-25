package com.woowacourse.friendogly.application

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.woowacourse.friendogly.BuildConfig
import com.woowacourse.friendogly.application.di.AppModule

class FriendoglyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIEND_ID)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        AppModule.setInstance(applicationContext)
    }
}
