package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.JwtToken

interface LocalRepository {
    suspend fun getJwtToken(): Result<JwtToken?>

    suspend fun saveJwtToken(jwtToken: JwtToken): Result<Unit>

    suspend fun deleteLocalData(): Result<Unit>
}
