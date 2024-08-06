package com.happy.friendogly.data.source

interface SettingDataSource {

    suspend fun saveAlarm(isSet: Boolean): Result<Unit>

    suspend fun getAlarm(): Result<Boolean>

    suspend fun deleteAlarm(): Result<Unit>
}
