package com.happy.friendogly.local.source

import com.happy.friendogly.data.model.JwtTokenDto
import com.happy.friendogly.data.source.TokenDataSource
import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.local.mapper.toData
import com.happy.friendogly.local.model.JwtTokenEntity
import kotlinx.coroutines.flow.first

class TokenDataSourceImpl(
    private val tokenManager: TokenManager,
) : TokenDataSource {
    override suspend fun getJwtToken(): Result<JwtTokenDto> =
        runCatching {
            val accessToken = tokenManager.accessToken.first()
            // TODO refreshToken 임시 값
            JwtTokenEntity(accessToken = accessToken, refreshToken = "").toData()
        }

    override suspend fun saveJwtToken(jwtTokenDto: JwtTokenDto): Result<Unit> =
        runCatching {
            val accessToken = jwtTokenDto.accessToken ?: return@runCatching
            tokenManager.saveAccessToken(accessToken)
        }

    override suspend fun deleteLocalData(): Result<Unit> =
        runCatching {
            tokenManager.deleteAccessToken()
        }
}
