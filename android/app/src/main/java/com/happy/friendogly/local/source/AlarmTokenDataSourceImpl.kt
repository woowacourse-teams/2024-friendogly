package com.happy.friendogly.local.source

import com.happy.friendogly.data.source.AlarmTokenDataSource
import com.happy.friendogly.local.di.AlarmTokenModule
import kotlinx.coroutines.flow.first

class AlarmTokenDataSourceImpl(
    private val alarmTokenModule: AlarmTokenModule
) : AlarmTokenDataSource {
    override suspend fun getToken(): Result<String> = runCatching {
        alarmTokenModule.token.first()
    }

    override suspend fun saveToken(token: String): Result<Unit> = runCatching {
        alarmTokenModule.saveToken(token)
    }

    override suspend fun deleteToken(): Result<Unit> = runCatching {
        alarmTokenModule.deleteToken()
    }
}
