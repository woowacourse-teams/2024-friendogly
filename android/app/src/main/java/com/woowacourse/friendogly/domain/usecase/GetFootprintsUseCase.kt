package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.domain.repository.FootprintRepository

class GetFootprintsUseCase(
    private val repository: FootprintRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>> = repository.getFootprints(latitude = latitude, longitude = longitude)
}
