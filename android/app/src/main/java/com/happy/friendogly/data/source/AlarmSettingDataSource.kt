package com.happy.friendogly.data.source

interface AlarmSettingDataSource {
    suspend fun saveChatSetting(isSet: Boolean): Result<Unit>

    suspend fun getChatSetting(): Result<Boolean>

    suspend fun deleteChatSetting(): Result<Unit>

    suspend fun saveWoofSetting(isSet: Boolean): Result<Unit>

    suspend fun getWoofSetting(): Result<Boolean>

    suspend fun deleteWoofSetting(): Result<Unit>
}
