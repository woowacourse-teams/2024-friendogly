package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.LoginDto
import com.happy.friendogly.data.source.AuthDataSource
import com.happy.friendogly.remote.api.AuthService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.PostLoginRequest

class AuthDataSourceImpl(
    private val service: AuthService,
) : AuthDataSource {
    override suspend fun postKakaoLogin(accessToken: String): Result<LoginDto> =
        runCatching {
            val body = PostLoginRequest(accessToken = accessToken)
            service.postKakaoLogin(body = body).data.toData()
        }
}
