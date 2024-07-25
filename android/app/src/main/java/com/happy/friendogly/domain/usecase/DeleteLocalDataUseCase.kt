package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.LocalRepository

class DeleteLocalDataUseCase(
    private val repository: LocalRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteLocalData()
}
