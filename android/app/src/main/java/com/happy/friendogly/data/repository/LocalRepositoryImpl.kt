package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.LocalDataSource
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.repository.LocalRepository

class LocalRepositoryImpl(
    private val source: LocalDataSource,
) : LocalRepository {
    override suspend fun getJwtToken(): Result<JwtToken> = source.getJwtToken().mapCatching { result -> result.toDomain() }

    override suspend fun saveJwtToken(jwtToken: JwtToken): Result<Unit> = source.saveJwtToken(jwtTokenDto = jwtToken.toData())

    override suspend fun deleteLocalData(): Result<Unit> = source.deleteLocalData()
}
