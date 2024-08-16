package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.TokenDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.repository.TokenRepository

class TokenRepositoryImpl(
    private val source: TokenDataSource,
) : TokenRepository {
    override suspend fun getJwtToken(): DomainResult<JwtToken?, DataError.Local> =
        source.getJwtToken().fold(
            onSuccess = { jwtTokenDto ->
                DomainResult.Success(jwtTokenDto.toDomain())
            },
            onFailure = {
                DomainResult.Error(DataError.Local.TOKEN_NOT_STORED)
            },
        )

    override suspend fun saveJwtToken(jwtToken: JwtToken): DomainResult<Unit, DataError.Local> =
        source.saveJwtToken(jwtTokenDto = jwtToken.toData()).fold(
            onSuccess = {
                DomainResult.Success(Unit)
            },
            onFailure = { _ ->
                DomainResult.Error(DataError.Local.TOKEN_NOT_STORED)
            },
        )

    override suspend fun deleteToken(): Result<Unit> = source.deleteLocalData()
}
