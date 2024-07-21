package com.woowacourse.friendogly.data.repository

import android.content.Context
import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.KakaoLoginDataSource
import com.woowacourse.friendogly.domain.model.KakaoAccessToken
import com.woowacourse.friendogly.domain.repository.KakaoLoginRepository

class KakaoLoginRepositoryImpl(
    private val dataSource: KakaoLoginDataSource,
) : KakaoLoginRepository {
    override suspend fun login(context: Context): Result<KakaoAccessToken> =
        dataSource.login(context = context).mapCatching { it.toDomain() }
}
