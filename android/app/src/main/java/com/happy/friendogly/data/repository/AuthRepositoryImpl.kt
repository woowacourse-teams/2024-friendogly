package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.ApiExceptionDto
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.repository.AuthRepository
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val source: AuthDataSource,
) : AuthRepository {
    override suspend fun postKakaoLogin(accessToken: String): DomainResult<Login, DataError.Network> {
        return source.postKakaoLogin(accessToken = accessToken).fold(
            onSuccess = { login ->
                DomainResult.Success(login.toDomain())
            },
            onFailure = { e ->
                when (e) {
                    is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                    is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )
    }

    override suspend fun postLogout(): DomainResult<Unit, DataError.Network> {
        return source.postLogout().fold(
            onSuccess = {
                DomainResult.Success(Unit)
            },
            onFailure = { e ->
                when (e) {
                    is ApiExceptionDto -> DomainResult.Error(e.error.data.errorCode.toDomain())
                    is ConnectException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    is UnknownHostException -> DomainResult.Error(DataError.Network.NO_INTERNET)
                    else -> DomainResult.Error(DataError.Network.SERVER_ERROR)
                }
            },
        )
    }
}
