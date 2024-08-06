package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.SettingRepository

class DeleteAlarmUseCase(
    private val repository: SettingRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteAlarm()
}
