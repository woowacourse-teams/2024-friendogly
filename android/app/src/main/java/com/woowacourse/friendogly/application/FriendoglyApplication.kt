package com.woowacourse.friendogly.application

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import com.woowacourse.friendogly.BuildConfig

class FriendoglyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initNaverMapSdk()
    }

    private fun initNaverMapSdk() {
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIEND_ID)
    }
}
