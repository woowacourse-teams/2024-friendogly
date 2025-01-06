package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.MyPlayground
import javax.inject.Inject

class GetMyPlaygroundUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(): DomainResult<MyPlayground, DataError.Network> = repository.getMyPlayground()
    }
