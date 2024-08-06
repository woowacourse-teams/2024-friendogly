package com.happy.friendogly.domain.repository

interface AlarmTokenRepository {
    suspend fun getToken(): Result<String>

    suspend fun saveToken(token: String): Result<Unit>

    suspend fun deleteToken(): Result<Unit>
}
