package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Footprint

interface FootprintRepository {
    suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit>

    suspend fun getFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>>
}
