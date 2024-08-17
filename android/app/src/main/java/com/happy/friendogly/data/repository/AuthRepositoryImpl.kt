package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val source: AuthDataSource,
) : AuthRepository {
    override suspend fun postKakaoLogin(accessToken: String): DomainResult<Login, DataError.Network> {
        return source.postKakaoLogin(accessToken = accessToken).fold(
            onSuccess = { login ->
                DomainResult.Success(login.toDomain())
            },
            onFailure = { e ->
                if (e is ApiExceptionDto) {
                    DomainResult.Error(e.error.data.errorCode.toDomain())
                } else {
                    DomainResult.Error(DataError.Network.NO_INTERNET)
                }
            },
        )
    }
}
