package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundArrival
import javax.inject.Inject

class PatchPlaygroundArrivalUseCase
    @Inject
    constructor(
        private val repository: WoofRepository,
    ) {
        suspend operator fun invoke(
            latitude: Double,
            longitude: Double,
        ): Result<PlaygroundArrival> = repository.patchPlaygroundArrival(latitude = latitude, longitude = longitude)
    }
