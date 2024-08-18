package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository

class GetWoofAlarmUseCase(
    private val repository: AlarmSettingRepository
) {
    suspend operator fun invoke():Result<Boolean> = repository.getWoofSetting()
}
