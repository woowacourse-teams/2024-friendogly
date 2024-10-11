package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import javax.inject.Inject

class GetPlaygroundInfoUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(id: Long): Result<PlaygroundInfo> = repository.getPlaygroundInfo(id)
    }
