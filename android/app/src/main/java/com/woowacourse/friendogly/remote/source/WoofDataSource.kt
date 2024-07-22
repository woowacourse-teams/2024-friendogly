package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.request.FootprintRequest

interface WoofDataSource {
    suspend fun postFootPrint(request: FootprintRequest): Result<Unit>

    suspend fun getFootPrintMarkBtnInfo(): Result<FootprintMarkBtnInfoDto>

    suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootprintDto>>

    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
