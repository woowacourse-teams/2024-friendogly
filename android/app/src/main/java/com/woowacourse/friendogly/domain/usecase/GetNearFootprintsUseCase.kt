package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.domain.repository.WoofRepository

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
