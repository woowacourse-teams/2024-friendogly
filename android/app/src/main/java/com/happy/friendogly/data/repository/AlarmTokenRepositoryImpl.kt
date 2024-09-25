package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.AlarmTokenDataSource
import com.happy.friendogly.domain.repository.AlarmTokenRepository
import javax.inject.Inject

class AlarmTokenRepositoryImpl
    @Inject
    constructor(private val source: AlarmTokenDataSource) : AlarmTokenRepository {
        override suspend fun saveToken(token: String): Result<Unit> = source.saveToken(token)
    }
