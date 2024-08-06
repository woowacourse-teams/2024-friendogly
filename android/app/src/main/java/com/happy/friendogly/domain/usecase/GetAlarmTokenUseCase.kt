package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmTokenRepository

class GetAlarmTokenUseCase(
    private val repository: AlarmTokenRepository
) {

    suspend operator fun invoke():Result<String> = repository.getToken()
}
