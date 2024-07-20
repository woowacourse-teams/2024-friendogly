package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toData
import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.LocalDataSource
import com.woowacourse.friendogly.domain.model.JwtToken
import com.woowacourse.friendogly.domain.repository.LocalRepository

class LocalRepositoryImpl(
    private val source: LocalDataSource,
) : LocalRepository {
    override suspend fun getJwtToken(): Result<JwtToken> = source.getJwtToken().mapCatching { result -> result.toDomain() }

    override suspend fun saveJwtToken(jwtToken: JwtToken): Result<Unit> = source.saveJwtToken(jwtTokenDto = jwtToken.toData())

    override suspend fun deleteLocalData(): Result<Unit> = source.deleteLocalData()
}
