package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Footprint
import com.happy.friendogly.domain.repository.WoofRepository

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
