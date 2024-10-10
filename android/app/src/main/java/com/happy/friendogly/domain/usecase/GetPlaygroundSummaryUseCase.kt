package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundSummary
import javax.inject.Inject

class GetPlaygroundSummaryUseCase
    @Inject
    constructor(private val repository: WoofRepository) {
        suspend operator fun invoke(playgroundId: Long): Result<PlaygroundSummary> = repository.getPlaygroundSummary(playgroundId)
    }
