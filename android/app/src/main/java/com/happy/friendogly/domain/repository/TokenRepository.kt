package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.JwtToken

interface TokenRepository {
    suspend fun getJwtToken(): DomainResult<JwtToken?, DataError.Local>

    suspend fun saveJwtToken(jwtToken: JwtToken): DomainResult<Unit, DataError.Local>

    suspend fun deleteToken(): Result<Unit>
}
