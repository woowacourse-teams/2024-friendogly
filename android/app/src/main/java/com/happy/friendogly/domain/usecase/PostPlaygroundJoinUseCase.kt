package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundJoin
import javax.inject.Inject

class PostPlaygroundJoinUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(playgroundId: Long): DomainResult<PlaygroundJoin, DataError.Network> =
            repository.postPlaygroundJoin(playgroundId)
    }
