package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.data.model.LandMarkDto

interface WoofDataSource {
    suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootPrintDto>>

    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
