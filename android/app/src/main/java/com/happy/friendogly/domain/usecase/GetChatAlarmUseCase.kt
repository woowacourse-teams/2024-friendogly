package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository
import javax.inject.Inject

class GetChatAlarmUseCase
    @Inject
    constructor(
        private val repository: AlarmSettingRepository,
    ) {
        suspend operator fun invoke(): Result<Boolean> = repository.getChatSetting()
    }
