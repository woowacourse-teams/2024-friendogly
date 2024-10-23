package com.happy.friendogly.data.source

interface AlarmSettingDataSource {
    suspend fun saveChatSetting(isSet: Boolean): Result<Unit>

    suspend fun getChatSetting(): Result<Boolean>

    suspend fun deleteChatSetting(): Result<Unit>

    suspend fun savePlaygroundSetting(isSet: Boolean): Result<Unit>

    suspend fun getPlaygroundSetting(): Result<Boolean>

    suspend fun deletePlaygroundSetting(): Result<Unit>
}
