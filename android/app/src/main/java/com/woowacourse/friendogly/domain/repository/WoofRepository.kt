package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.domain.model.LandMark

interface WoofRepository {
    suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit>

    suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfo>

    suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>>

    suspend fun getLandMarks(): Result<List<LandMark>>
}
