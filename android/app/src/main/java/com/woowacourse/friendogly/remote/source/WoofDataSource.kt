package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.data.model.FootPrintMineLatestDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.request.FootPrintRequest

interface WoofDataSource {
    suspend fun postFootPrint(request: FootPrintRequest): Result<Unit>

    suspend fun getMyLatestFootPrintTime(): Result<FootPrintMineLatestDto>

    suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootPrintDto>>

    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
