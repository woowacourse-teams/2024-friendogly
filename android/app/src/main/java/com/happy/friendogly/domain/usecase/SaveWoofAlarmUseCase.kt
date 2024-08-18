package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository

class SaveWoofAlarmUseCase(
    private val repository: AlarmSettingRepository
) {
    suspend operator fun invoke(isSet: Boolean): Result<Unit> = repository.saveWoofSetting(isSet)
}
