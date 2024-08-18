package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.domain.repository.AlarmSettingRepository

class AlarmSettingRepositoryImpl(private val source: AlarmSettingDataSource) :
    AlarmSettingRepository {

    override suspend fun saveChatSetting(isSet: Boolean): Result<Unit> =
        source.saveChatSetting(isSet)

    override suspend fun getChatSetting(): Result<Boolean> = source.getChatSetting()

    override suspend fun deleteChatSetting(): Result<Unit> = source.deleteChatSetting()

    override suspend fun saveWoofSetting(isSet: Boolean): Result<Unit> =
        source.saveWoofSetting(isSet)

    override suspend fun getWoofSetting(): Result<Boolean> = source.getWoofSetting()

    override suspend fun deleteWoofSetting(): Result<Unit> = source.deleteWoofSetting()
}
