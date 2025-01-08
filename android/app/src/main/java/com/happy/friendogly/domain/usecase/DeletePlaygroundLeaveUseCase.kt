package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.PlaygroundRepository
import javax.inject.Inject

class DeletePlaygroundLeaveUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(): DomainResult<Unit, DataError.Network> = repository.deletePlaygroundLeave()
    }
