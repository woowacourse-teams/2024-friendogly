package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.SettingRepository

class GetAlarmUseCase(
    private val repository: SettingRepository
) {

    suspend operator fun invoke():Result<Boolean> = repository.getAlarm()
}
