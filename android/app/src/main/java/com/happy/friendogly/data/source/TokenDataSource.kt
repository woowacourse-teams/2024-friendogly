package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.JwtTokenDto

interface TokenDataSource {
    suspend fun getJwtToken(): Result<JwtTokenDto>

    suspend fun saveJwtToken(jwtTokenDto: JwtTokenDto): Result<Unit>

    suspend fun deleteLocalData(): Result<Unit>
}
