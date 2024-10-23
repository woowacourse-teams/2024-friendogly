package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.data.model.PlaygroundArrivalDto
import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.data.model.PlaygroundJoinDto
import com.happy.friendogly.data.model.PlaygroundMessageDto
import com.happy.friendogly.data.model.PlaygroundSummaryDto
import com.happy.friendogly.remote.model.request.PatchPlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PatchPlaygroundMessageRequest
import com.happy.friendogly.remote.model.request.PostPlaygroundRequest

interface PlaygroundDataSource {
    suspend fun postPlayground(request: PostPlaygroundRequest): Result<MyPlaygroundDto>

    suspend fun patchPlaygroundArrival(request: PatchPlaygroundArrivalRequest): Result<PlaygroundArrivalDto>

    suspend fun getPetExistence(): Result<PetExistenceDto>

    suspend fun getNearPlaygrounds(): Result<List<PlaygroundDto>>

    suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfoDto>

    suspend fun getPlaygroundSummary(playgroundId: Long): Result<PlaygroundSummaryDto>

    suspend fun postPlaygroundJoin(playgroundId: Long): Result<PlaygroundJoinDto>

    suspend fun deletePlaygroundLeave(): Result<Unit>

    suspend fun patchPlaygroundMessage(request: PatchPlaygroundMessageRequest): Result<PlaygroundMessageDto>
}
