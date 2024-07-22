package com.woowacourse.friendogly.application

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.woowacourse.friendogly.BuildConfig
import com.woowacourse.friendogly.application.di.AppModule
import com.woowacourse.friendogly.data.client.RetrofitClient
import com.woowacourse.friendogly.data.source.FootprintDataSourceImpl
import com.woowacourse.friendogly.data.source.WoofDataSourceImpl
import com.woowacourse.friendogly.remote.service.FootprintService
import com.woowacourse.friendogly.remote.service.WoofService
import com.woowacourse.friendogly.remote.source.FootprintDataSource
import com.woowacourse.friendogly.remote.source.WoofDataSource

class FriendoglyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initNaverMapSdk()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        AppModule.setInstance(applicationContext)

        val retrofit = RetrofitClient.getInstance()
        val footPrintService = retrofit.create(FootprintService::class.java)
        val woofService = retrofit.create(WoofService::class.java)
        remoteFootprintDataSource = FootprintDataSourceImpl(footPrintService)
        remoteWoofDataSource = WoofDataSourceImpl(woofService)
    }

    private fun initNaverMapSdk() {
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIEND_ID)
    }

    companion object {
        lateinit var remoteFootprintDataSource: FootprintDataSource
        lateinit var remoteWoofDataSource: WoofDataSource
    }
}
