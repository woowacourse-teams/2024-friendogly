package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.FootprintRecentWalkStatusDto
import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusAutoRequest
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusManualRequest
import com.happy.friendogly.remote.model.request.PlaygroundRequest

interface WoofDataSource {
    suspend fun postPlayground(request: PlaygroundRequest): Result<MyPlaygroundDto>

    suspend fun patchFootprintRecentWalkStatusAuto(request: FootprintRecentWalkStatusAutoRequest): Result<FootprintRecentWalkStatusDto>

    suspend fun patchFootprintRecentWalkStatusManual(request: FootprintRecentWalkStatusManualRequest): Result<FootprintRecentWalkStatusDto>

    suspend fun getFootprintMarkBtnInfo(): Result<PetExistenceDto>

    suspend fun getNearFootprints(): Result<List<PlaygroundDto>>

    suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfoDto>

    suspend fun deleteFootprint(footprintId: Long): Result<Unit>
}
