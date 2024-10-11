package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.PlaygroundRepository
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundArrival
import javax.inject.Inject

class PatchPlaygroundArrivalUseCase
    @Inject
    constructor(
        private val repository: PlaygroundRepository,
    ) {
        suspend operator fun invoke(
            latitude: Double,
            longitude: Double,
        ): Result<PlaygroundArrival> = repository.patchPlaygroundArrival(latitude = latitude, longitude = longitude)
    }
