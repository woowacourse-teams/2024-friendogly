package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.FootprintSave
import com.happy.friendogly.domain.repository.WoofRepository

class PostFootprintUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<FootprintSave> =
        repository.postFootprint(
            latitude,
            longitude,
        )
}
