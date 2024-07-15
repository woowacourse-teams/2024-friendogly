package com.woowacourse.friendogly.application

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import com.woowacourse.friendogly.BuildConfig

class FriendoglyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.kakao_app_key)
    }
}
