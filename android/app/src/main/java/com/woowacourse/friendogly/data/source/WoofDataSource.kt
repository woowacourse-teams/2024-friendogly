package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.data.model.FootprintSaveDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.request.FootprintRequest

interface WoofDataSource {
    suspend fun postFootprint(request: FootprintRequest): Result<FootprintSaveDto>

    suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfoDto>

    suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootprintDto>>

    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
