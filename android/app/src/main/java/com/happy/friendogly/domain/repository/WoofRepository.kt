package com.happy.friendogly.domain.repository

import com.happy.friendogly.presentation.ui.woof.model.MyPlayground
import com.happy.friendogly.presentation.ui.woof.model.PetExistence
import com.happy.friendogly.presentation.ui.woof.model.Playground
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundArrival
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo

interface WoofRepository {
    suspend fun postPlayground(
        latitude: Double,
        longitude: Double,
    ): Result<MyPlayground>

    suspend fun patchPlaygroundArrival(
        latitude: Double,
        longitude: Double,
    ): Result<PlaygroundArrival>

    suspend fun getFootprintMarkBtnInfo(): Result<PetExistence>

    suspend fun getPlaygrounds(): Result<List<Playground>>

    suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfo>

    suspend fun deleteFootprint(footprintId: Long): Result<Unit>
}
