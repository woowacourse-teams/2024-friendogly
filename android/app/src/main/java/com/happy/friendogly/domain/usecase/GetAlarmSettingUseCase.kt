package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository

class GetAlarmSettingUseCase(
    private val repository: AlarmSettingRepository,
) {
    suspend operator fun invoke(): Result<Boolean> = repository.getAlarm()
}
