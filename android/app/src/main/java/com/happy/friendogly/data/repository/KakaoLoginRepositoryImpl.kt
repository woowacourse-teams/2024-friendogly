package com.happy.friendogly.data.repository

import android.content.Context
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.KakaoLoginDataSource
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.repository.KakaoLoginRepository

class KakaoLoginRepositoryImpl(
    private val dataSource: KakaoLoginDataSource,
) : KakaoLoginRepository {
    override suspend fun login(context: Context): Result<KakaoAccessToken> =
        dataSource.login(context = context).mapCatching { it.toDomain() }
}
