package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Login

interface AuthRepository {
    suspend fun postKakaoLogin(accessToken: String): DomainResult<Login, DataError.Network>

    suspend fun postLogout(): DomainResult<Unit, DataError.Network>
}
