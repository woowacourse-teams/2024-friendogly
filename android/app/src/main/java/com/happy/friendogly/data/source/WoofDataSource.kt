package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.data.model.PlaygroundArrivalDto
import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.data.model.PlaygroundSummaryDto
import com.happy.friendogly.remote.model.request.PlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PlaygroundRequest

interface WoofDataSource {
    suspend fun postPlayground(request: PlaygroundRequest): Result<MyPlaygroundDto>

    suspend fun patchPlaygroundArrival(request: PlaygroundArrivalRequest): Result<PlaygroundArrivalDto>

    suspend fun getFootprintMarkBtnInfo(): Result<PetExistenceDto>

    suspend fun getNearFootprints(): Result<List<PlaygroundDto>>

    suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfoDto>

    suspend fun getPlaygroundSummary(id: Long): Result<PlaygroundSummaryDto>

    suspend fun deleteFootprint(footprintId: Long): Result<Unit>
}
