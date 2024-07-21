package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.JwtTokenDto

interface LocalDataSource {
    suspend fun getJwtToken(): Result<JwtTokenDto>

    suspend fun saveJwtToken(jwtTokenDto: JwtTokenDto): Result<Unit>

    suspend fun deleteLocalData(): Result<Unit>
}
