package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.domain.Result
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val source: AuthDataSource,
) : AuthRepository {
    override suspend fun postKakaoLogin(accessToken: String): Result<Login, DataError.Network> {
        return source.postKakaoLogin(accessToken = accessToken).fold(
            onSuccess = { login ->
                Result.Success(login.toDomain())
            },
            onFailure = { e ->
                if (e is ApiExceptionDto) {
                    Result.Error(e.error.data.errorCode.toDomain())
                } else {
                    Result.Error(DataError.Network.NO_INTERNET)
                }
            },
        )
    }
}
