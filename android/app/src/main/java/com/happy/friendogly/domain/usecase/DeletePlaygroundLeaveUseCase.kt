package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import javax.inject.Inject

class DeletePlaygroundLeaveUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(): Result<Unit> = repository.deletePlaygroundLeave()
    }
