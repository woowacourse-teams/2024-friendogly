package com.happy.friendogly.data.repository

import android.content.Context
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.KakaoLoginDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.repository.KakaoLoginRepository

class KakaoLoginRepositoryImpl(
    private val dataSource: KakaoLoginDataSource,
) : KakaoLoginRepository {
    override suspend fun login(context: Context): DomainResult<KakaoAccessToken, DataError.Local> =
        dataSource.login(context = context).fold(
            onSuccess = { kakaoAccessTokenDto ->
                DomainResult.Success(kakaoAccessTokenDto.toDomain())
            },
            onFailure = {
                DomainResult.Error(DataError.Local.LOCAL_ERROR)
            },
        )
}
