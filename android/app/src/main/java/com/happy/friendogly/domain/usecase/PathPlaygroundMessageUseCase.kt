package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundMessage
import javax.inject.Inject

class PathPlaygroundMessageUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(message: String): Result<PlaygroundMessage> = repository.patchPlaygroundMessage(message)
    }
