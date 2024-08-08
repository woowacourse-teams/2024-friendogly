package com.happy.friendogly.data.source

interface AlarmTokenDataSource {
    suspend fun saveToken(token: String): Result<Unit>
}
