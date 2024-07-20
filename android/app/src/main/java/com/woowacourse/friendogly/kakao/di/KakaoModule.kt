package com.woowacourse.friendogly.kakao.di

import com.woowacourse.friendogly.data.repository.KakaoLoginRepositoryImpl
import com.woowacourse.friendogly.data.source.KakaoLoginDataSource
import com.woowacourse.friendogly.domain.repository.KakaoLoginRepository
import com.woowacourse.friendogly.domain.usecase.KakaoLoginUseCase
import com.woowacourse.friendogly.kakao.source.KakaoLoginDataSourceImpl

class KakaoModule {
    private val kakaoLoginDataSource: KakaoLoginDataSource = KakaoLoginDataSourceImpl()

    private val kakaoLoginRepository: KakaoLoginRepository =
        KakaoLoginRepositoryImpl(dataSource = kakaoLoginDataSource)

    val kakaoLoginUseCase: KakaoLoginUseCase = KakaoLoginUseCase(repository = kakaoLoginRepository)
}
