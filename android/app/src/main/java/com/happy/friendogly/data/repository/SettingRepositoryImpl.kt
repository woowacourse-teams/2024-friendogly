package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.SettingDataSource
import com.happy.friendogly.domain.repository.SettingRepository

class SettingRepositoryImpl(private val source: SettingDataSource) : SettingRepository {
    override suspend fun saveAlarm(isSet: Boolean): Result<Unit> = source.saveAlarm(isSet)

    override suspend fun getAlarm(): Result<Boolean> = source.getAlarm()

    override suspend fun deleteAlarm(): Result<Unit> = source.deleteAlarm()
}
