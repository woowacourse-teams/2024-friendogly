package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmTokenRepository

class DeleteAlarmTokenUseCase(
    private val repository: AlarmTokenRepository
) {
    suspend operator fun invoke():Result<Unit> = repository.deleteToken()
}
