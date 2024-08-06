package com.happy.friendogly.local.source

import com.happy.friendogly.data.source.SettingDataSource
import com.happy.friendogly.local.di.AlarmModule
import kotlinx.coroutines.flow.first

class SettingDataSourceImpl(
    private val alarmModule: AlarmModule,
) : SettingDataSource {
    override suspend fun saveAlarm(isSet: Boolean): Result<Unit> =
        runCatching {
            alarmModule.saveSetting(isSet)
        }

    override suspend fun getAlarm(): Result<Boolean> =
        runCatching {
            alarmModule.isSet.first()
        }

    override suspend fun deleteAlarm(): Result<Unit> =
        runCatching {
            alarmModule.deleteSetting()
        }
}
