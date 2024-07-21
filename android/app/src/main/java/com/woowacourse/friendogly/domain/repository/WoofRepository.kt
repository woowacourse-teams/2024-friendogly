package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.domain.model.FootPrintMarkBtnInfo
import com.woowacourse.friendogly.domain.model.LandMark

interface WoofRepository {
    suspend fun postFootPrint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit>

    suspend fun getFootPrintMarkBtnInfo(): Result<FootPrintMarkBtnInfo>

    suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootPrint>>

    suspend fun getLandMarks(): Result<List<LandMark>>
}
