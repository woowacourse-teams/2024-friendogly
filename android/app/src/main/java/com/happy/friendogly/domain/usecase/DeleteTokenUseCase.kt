package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.TokenRepository

class DeleteTokenUseCase(
    private val repository: TokenRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteToken()
}
