package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import javax.inject.Inject

class GetNearFootprintsUseCase @Inject constructor(private val repository: WoofRepository) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>> =
        repository.getNearFootprints(
            latitude,
            longitude,
        )
}
