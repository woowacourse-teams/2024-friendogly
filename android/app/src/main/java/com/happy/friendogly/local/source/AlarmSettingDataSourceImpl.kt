package com.happy.friendogly.local.source

import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.local.di.ChatAlarmModule
import com.happy.friendogly.local.di.PlaygroundAlarmModule
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AlarmSettingDataSourceImpl
    @Inject
    constructor(
        private val chatAlarmModule: ChatAlarmModule,
        private val playgroundAlarmModule: PlaygroundAlarmModule,
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

        override suspend fun savePlaygroundSetting(isSet: Boolean): Result<Unit> =
            runCatching {
                playgroundAlarmModule.saveSetting(isSet)
            }

        override suspend fun getPlaygroundSetting(): Result<Boolean> =
            runCatching {
                playgroundAlarmModule.isSet.first()
            }

        override suspend fun deletePlaygroundSetting(): Result<Unit> =
            runCatching {
                playgroundAlarmModule.deleteSetting()
            }
    }
