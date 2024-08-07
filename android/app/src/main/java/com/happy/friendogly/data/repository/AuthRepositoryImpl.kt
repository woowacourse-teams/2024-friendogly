package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val source: AuthDataSource,
) : AuthRepository {
    override suspend fun postKakaoLogin(accessToken: String): Result<Login> =
        source.postKakaoLogin(accessToken = accessToken).mapCatching { result -> result.toDomain() }
}
