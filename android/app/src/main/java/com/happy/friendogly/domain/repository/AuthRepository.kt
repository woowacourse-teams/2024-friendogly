package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.Result
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Login

interface AuthRepository {
    suspend fun postKakaoLogin(accessToken: String): Result<Login, DataError.Network>
}
