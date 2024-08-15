package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.AlarmTokenDataSource
import com.happy.friendogly.domain.repository.AlarmTokenRepository

class AlarmTokenRepositoryImpl(private val source: AlarmTokenDataSource) : AlarmTokenRepository {
    override suspend fun saveToken(token: String): Result<Unit> = source.saveToken(token)
}
