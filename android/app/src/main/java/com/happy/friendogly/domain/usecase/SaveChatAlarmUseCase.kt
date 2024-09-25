package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmSettingRepository
import javax.inject.Inject

class SaveChatAlarmUseCase @Inject constructor(
    private val repository: AlarmSettingRepository,
) {
    suspend operator fun invoke(isSet: Boolean): Result<Unit> = repository.saveChatSetting(isSet)
}
