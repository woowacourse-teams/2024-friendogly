package com.woowacourse.friendogly

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.woowacourse.friendogly.kakao.di.KakaoModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    companion object {
        val kakaoModule: KakaoModule = KakaoModule()
    }
}
