package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository

class DeleteChatAlarmUseCase(
    private val repository: AlarmSettingRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteChatSetting()
}