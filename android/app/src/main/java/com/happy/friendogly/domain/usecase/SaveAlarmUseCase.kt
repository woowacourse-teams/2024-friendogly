package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.SettingRepository

class SaveAlarmUseCase(
    private val repository: SettingRepository
) {
    suspend operator fun invoke(isSet: Boolean): Result<Unit> = repository.saveAlarm(isSet)
}
