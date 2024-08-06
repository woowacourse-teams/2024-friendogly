package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AlarmTokenRepository

class SaveAlamTokenUseCase(
    private val repository: AlarmTokenRepository
) {

    suspend operator fun invoke(token: String): Result<Unit> = repository.saveToken(token)
}
