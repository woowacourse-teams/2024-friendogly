package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.repository.LocalRepository

class DeleteLocalDataUseCase(
    private val repository: LocalRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteLocalData()
}
