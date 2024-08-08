package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.TokenDataSource
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.repository.TokenRepository

class TokenRepositoryImpl(
    private val source: TokenDataSource,
) : TokenRepository {
    override suspend fun getJwtToken(): Result<JwtToken> = source.getJwtToken().mapCatching { result -> result.toDomain() }

    override suspend fun saveJwtToken(jwtToken: JwtToken): Result<Unit> = source.saveJwtToken(jwtTokenDto = jwtToken.toData())

    override suspend fun deleteToken(): Result<Unit> = source.deleteLocalData()
}
