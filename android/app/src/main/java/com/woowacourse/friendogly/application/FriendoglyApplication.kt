package com.woowacourse.friendogly.application

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.woowacourse.friendogly.BuildConfig
import com.woowacourse.friendogly.application.di.AppModule
import com.woowacourse.friendogly.kakao.di.KakaoModule

class FriendoglyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initNaverMapSdk()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        AppModule.setInstance(applicationContext)
    }

    private fun initNaverMapSdk() {
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIEND_ID)
    }

    companion object {
        val kakaoModule: KakaoModule = KakaoModule()
    }
}
