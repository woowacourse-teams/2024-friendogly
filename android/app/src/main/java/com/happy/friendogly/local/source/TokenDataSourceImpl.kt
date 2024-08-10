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
            val refreshToken = tokenManager.refreshToken.first()

            JwtTokenEntity(accessToken = accessToken, refreshToken = refreshToken).toData()
        }

    override suspend fun saveJwtToken(jwtTokenDto: JwtTokenDto): Result<Unit> =
        runCatching {
            val accessToken = jwtTokenDto.accessToken ?: return@runCatching
            val refreshToken = jwtTokenDto.refreshToken ?: return@runCatching

            tokenManager.saveAccessToken(accessToken)
            tokenManager.saveRefreshToken(refreshToken)
        }

    override suspend fun deleteLocalData(): Result<Unit> =
        runCatching {
            tokenManager.deleteToken()
        }
}
