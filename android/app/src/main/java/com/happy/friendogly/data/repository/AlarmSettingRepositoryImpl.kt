package com.happy.friendogly.data.repository

import com.happy.friendogly.data.source.AlarmSettingDataSource
import com.happy.friendogly.domain.repository.AlarmSettingRepository
import javax.inject.Inject

class AlarmSettingRepositoryImpl
    @Inject
    constructor(private val source: AlarmSettingDataSource) :
    AlarmSettingRepository {
        override suspend fun saveChatSetting(isSet: Boolean): Result<Unit> = source.saveChatSetting(isSet)

        override suspend fun getChatSetting(): Result<Boolean> = source.getChatSetting()

        override suspend fun deleteChatSetting(): Result<Unit> = source.deleteChatSetting()

        override suspend fun savePlaygroundSetting(isSet: Boolean): Result<Unit> = source.savePlaygroundSetting(isSet)

        override suspend fun getPlaygroundSetting(): Result<Boolean> = source.getPlaygroundSetting()

        override suspend fun deletePlaygroundSetting(): Result<Unit> = source.deletePlaygroundSetting()
    }
