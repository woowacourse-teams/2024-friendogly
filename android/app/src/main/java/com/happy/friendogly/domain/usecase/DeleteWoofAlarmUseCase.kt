package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository

class DeleteWoofAlarmUseCase(
    private val repository: AlarmSettingRepository
) {
    suspend operator fun invoke():Result<Unit> = repository.deleteWoofSetting()
}
