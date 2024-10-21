package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary
import javax.inject.Inject

class GetPlaygroundSummaryUseCase
    @Inject
    constructor(private val repository: PlaygroundRepository) {
        suspend operator fun invoke(playgroundId: Long): Result<PlaygroundSummary> = repository.getPlaygroundSummary(playgroundId)
    }
