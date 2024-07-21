package com.woowacourse.friendogly.local.source

import com.woowacourse.friendogly.data.model.JwtTokenDto
import com.woowacourse.friendogly.data.source.LocalDataSource
import com.woowacourse.friendogly.local.di.LocalModule
import com.woowacourse.friendogly.local.mapper.toData
import com.woowacourse.friendogly.local.model.JwtTokenEntity
import kotlinx.coroutines.flow.first

class LocalDataSourceImpl(
    private val localModule: LocalModule,
) : LocalDataSource {
    override suspend fun getJwtToken(): Result<JwtTokenDto> =
        runCatching {
            val accessToken = localModule.accessToken.first()
            // TODO refreshToken 임시 값
            JwtTokenEntity(accessToken = accessToken, refreshToken = "").toData()
        }

    override suspend fun saveJwtToken(jwtTokenDto: JwtTokenDto): Result<Unit> =
        runCatching {
            val accessToken = jwtTokenDto.accessToken ?: return@runCatching
            localModule.saveAccessToken(accessToken)
        }

    override suspend fun deleteLocalData(): Result<Unit> =
        runCatching {
            localModule.deleteAccessToken()
        }
}
