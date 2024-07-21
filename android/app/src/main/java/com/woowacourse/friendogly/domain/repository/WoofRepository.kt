package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.domain.model.FootPrintMineLatest
import com.woowacourse.friendogly.domain.model.LandMark

interface WoofRepository {
    suspend fun postFootPrint(
        latitude: Double,
        longitude: Double,
    ): Result<Unit>

    suspend fun getMyLatestFootPrintTime(): Result<FootPrintMineLatest>

    suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootPrint>>

    suspend fun getLandMarks(): Result<List<LandMark>>
}
