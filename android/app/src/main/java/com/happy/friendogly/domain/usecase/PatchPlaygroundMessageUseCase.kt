package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundMessage
import javax.inject.Inject

class PatchPlaygroundMessageUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(message: String): DomainResult<PlaygroundMessage, DataError.Network> =
            repository.patchPlaygroundMessage(message)
    }
