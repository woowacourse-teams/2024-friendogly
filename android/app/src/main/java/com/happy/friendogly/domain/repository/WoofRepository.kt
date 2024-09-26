package com.happy.friendogly.domain.repository

import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.FootprintMarkBtnInfo
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.MyFootprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus

interface WoofRepository {
    suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<MyFootprint>

    suspend fun patchFootprintRecentWalkStatusAuto(
        latitude: Double,
        longitude: Double,
    ): Result<FootprintRecentWalkStatus>

    suspend fun patchFootprintRecentWalkStatusManual(walkStatus: WalkStatus): Result<FootprintRecentWalkStatus>

    suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfo>

    suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>>

    suspend fun getFootprintInfo(footprintId: Long): Result<PlaygroundInfo>

    suspend fun deleteFootprint(footprintId: Long): Result<Unit>
}
