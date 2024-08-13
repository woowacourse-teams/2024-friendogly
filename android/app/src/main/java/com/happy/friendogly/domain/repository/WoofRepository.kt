package com.happy.friendogly.domain.repository

import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.model.FootprintMarkBtnInfo
import com.happy.friendogly.presentation.ui.woof.model.MyFootprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus

interface WoofRepository {
    suspend fun postFootprint(
        latitude: Double,
        longitude: Double,
    ): Result<MyFootprint>

    suspend fun patchWalkStatus(
        latitude: Double,
        longitude: Double,
    ): Result<WalkStatus>

    suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfo>

    suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<Footprint>>

    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfo>
}
