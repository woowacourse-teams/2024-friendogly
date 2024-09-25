package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository
import javax.inject.Inject

class DeleteWoofAlarmUseCase @Inject constructor(
    private val repository: AlarmSettingRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteWoofSetting()
}
