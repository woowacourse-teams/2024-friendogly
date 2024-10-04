package com.happy.friendogly.domain.repository

import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.MyPlayground
import com.happy.friendogly.presentation.ui.woof.model.PetExistence
import com.happy.friendogly.presentation.ui.woof.model.Playground
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus

interface WoofRepository {
    suspend fun postPlayground(
        latitude: Double,
        longitude: Double,
    ): Result<MyPlayground>

    suspend fun patchFootprintRecentWalkStatusAuto(
        latitude: Double,
        longitude: Double,
    ): Result<FootprintRecentWalkStatus>

    suspend fun patchFootprintRecentWalkStatusManual(walkStatus: WalkStatus): Result<FootprintRecentWalkStatus>

    suspend fun getFootprintMarkBtnInfo(): Result<PetExistence>

    suspend fun getPlaygrounds(): Result<List<Playground>>

    suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfo>

    suspend fun deleteFootprint(footprintId: Long): Result<Unit>
}
