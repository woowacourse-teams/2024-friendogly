package com.woowacourse.friendogly

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.woowacourse.friendogly.data.datasource.KakaoLoginDataSource
import com.woowacourse.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.woowacourse.friendogly.domain.repository.KakaoLoginRepository
import com.woowacourse.friendogly.kakao.datasource.KakaoLoginDataSourceImpl

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        initDataSources()
        initRepositories()
    }

    private fun initDataSources() {
        KakaoLoginDataSource.setInstance(KakaoLoginDataSourceImpl())
    }

    private fun initRepositories() {
        val kakaoLoginRepositoryImpl =
            KakaoLoginRepositoryImpl(dataSource = KakaoLoginDataSource.getInstance())
        KakaoLoginRepository.setInstance(kakaoLoginRepositoryImpl)
    }
}
