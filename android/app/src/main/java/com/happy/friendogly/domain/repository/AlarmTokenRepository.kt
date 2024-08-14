package com.happy.friendogly.domain.repository

interface AlarmTokenRepository {
    suspend fun saveToken(token: String): Result<Unit>
}
