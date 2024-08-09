package com.happy.friendogly.data.source

interface AlarmSettingDataSource {
    suspend fun saveAlarm(isSet: Boolean): Result<Unit>

    suspend fun getAlarm(): Result<Boolean>

    suspend fun deleteAlarm(): Result<Unit>
}
