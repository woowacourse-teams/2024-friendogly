package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.domain.model.LandMark

interface WoofRepository {
    suspend fun postFootPrint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit>

    suspend fun getFootPrintMarkBtnInfo(): Result<FootprintMarkBtnInfo>

    suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>>

    suspend fun getLandMarks(): Result<List<LandMark>>
}
