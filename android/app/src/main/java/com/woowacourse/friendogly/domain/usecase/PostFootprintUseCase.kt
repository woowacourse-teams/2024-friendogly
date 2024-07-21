package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.repository.FootprintRepository

class PostFootprintUseCase(
    private val repository: FootprintRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
    ): Result<Unit> = repository.postFootprint(latitude = latitude, longitude = longitude)
}
