package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.LoginDto
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.remote.api.AuthService
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.PostLoginRequest

class AuthDataSourceImpl(
    private val service: AuthService,
) : AuthDataSource {
    override suspend fun postKakaoLogin(accessToken: String): Result<LoginDto> {
        val body = PostLoginRequest(accessToken = accessToken)
        val result = runCatching { service.postKakaoLogin(body = body).data.toData() }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> Result.failure(exception.toData())
            else -> throw exception
        }
    }

    override suspend fun postLogout(): Result<Unit> =
        runCatching {
            val result = runCatching { service.postLogout() }

            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                else -> throw exception
            }
        }
}
