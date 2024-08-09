package com.happy.friendogly.domain.repository

interface AlarmSettingRepository {
    suspend fun saveAlarm(isSet: Boolean): Result<Unit>

    suspend fun getAlarm(): Result<Boolean>

    suspend fun deleteAlarm(): Result<Unit>
}
