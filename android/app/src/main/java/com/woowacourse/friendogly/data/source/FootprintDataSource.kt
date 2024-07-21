package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.FootprintDto

interface FootprintDataSource {
    suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit>

    suspend fun getFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootprintDto>>
}
