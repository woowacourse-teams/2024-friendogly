package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.domain.repository.AlarmSettingRepository

class AlarmSettingRepositoryImpl(private val source: AlarmSettingDataSource) : AlarmSettingRepository {
    override suspend fun saveAlarm(isSet: Boolean): Result<Unit> = source.saveAlarm(isSet)

    override suspend fun getAlarm(): Result<Boolean> = source.getAlarm()

    override suspend fun deleteAlarm(): Result<Unit> = source.deleteAlarm()
}
