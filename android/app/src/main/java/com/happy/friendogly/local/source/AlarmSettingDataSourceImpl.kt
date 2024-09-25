package com.happy.friendogly.local.source

import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.local.di.ChatAlarmModule
import com.happy.friendogly.local.di.WoofAlarmModule
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AlarmSettingDataSourceImpl @Inject constructor(
    private val chatAlarmModule: ChatAlarmModule,
    private val woofAlarmModule: WoofAlarmModule,
) : AlarmSettingDataSource {
    override suspend fun saveChatSetting(isSet: Boolean): Result<Unit> =
        runCatching {
            chatAlarmModule.saveSetting(isSet)
        }

    override suspend fun getChatSetting(): Result<Boolean> =
        runCatching {
            chatAlarmModule.isSet.first()
        }

    override suspend fun deleteChatSetting(): Result<Unit> =
        runCatching {
            chatAlarmModule.deleteSetting()
        }

    override suspend fun saveWoofSetting(isSet: Boolean): Result<Unit> =
        runCatching {
            woofAlarmModule.saveSetting(isSet)
        }

    override suspend fun getWoofSetting(): Result<Boolean> =
        runCatching {
            woofAlarmModule.isSet.first()
        }

    override suspend fun deleteWoofSetting(): Result<Unit> =
        runCatching {
            woofAlarmModule.deleteSetting()
        }
}
