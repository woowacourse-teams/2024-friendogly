package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.LoginDto

interface AuthDataSource {
    suspend fun postKakaoLogin(accessToken: String): Result<LoginDto>

    suspend fun postLogout(): Result<Unit>
}
