package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.FootprintSave
import com.woowacourse.friendogly.domain.repository.WoofRepository

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
