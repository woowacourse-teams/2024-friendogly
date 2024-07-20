package com.woowacourse.friendogly.application

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.woowacourse.friendogly.BuildConfig
import com.woowacourse.friendogly.application.di.AppModule
import com.woowacourse.friendogly.kakao.di.KakaoModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        AppModule.setInstance(applicationContext)
    }

    companion object {
        val kakaoModule: KakaoModule = KakaoModule()
    }
}
