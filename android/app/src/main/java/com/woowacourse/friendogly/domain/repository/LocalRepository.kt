package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.JwtToken

interface LocalRepository {
    suspend fun getJwtToken(): Result<JwtToken?>

    suspend fun saveJwtToken(jwtToken: JwtToken): Result<Unit>

    suspend fun deleteLocalData(): Result<Unit>
}
