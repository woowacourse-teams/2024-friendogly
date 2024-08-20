package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository

class GetChatAlarmUseCase(
    private val repository: AlarmSettingRepository,
) {
    suspend operator fun invoke(): Result<Boolean> = repository.getChatSetting()
}
