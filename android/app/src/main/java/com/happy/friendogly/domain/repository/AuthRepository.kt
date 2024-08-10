package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Login

interface AuthRepository {
    suspend fun postKakaoLogin(accessToken: String): Result<Login>
}
