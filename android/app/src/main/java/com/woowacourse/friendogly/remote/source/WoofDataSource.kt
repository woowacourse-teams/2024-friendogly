package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.request.FootPrintRequest

interface WoofDataSource {
    suspend fun postFootPrint(footPrintRequest: FootPrintRequest): Result<Unit>

    suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootPrintDto>>

    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
