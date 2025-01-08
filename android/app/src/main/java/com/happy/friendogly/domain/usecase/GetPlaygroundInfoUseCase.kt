package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import javax.inject.Inject

class GetPlaygroundInfoUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(id: Long): DomainResult<PlaygroundInfo, DataError.Network> = repository.getPlaygroundInfo(id)
    }
