package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import javax.inject.Inject

class DeletePlaygroundLeaveUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(): Result<Unit> = repository.deletePlaygroundLeave()
    }
