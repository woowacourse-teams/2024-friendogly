package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository

class DeleteAlarmSettingUseCase(
    private val repository: AlarmSettingRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteAlarm()
}
