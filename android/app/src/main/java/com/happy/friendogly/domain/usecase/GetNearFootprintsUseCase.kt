package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.Footprint

class GetNearFootprintsUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>> =
        repository.getNearFootprints(
            latitude,
            longitude,
        )
}
