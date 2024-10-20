package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.data.model.PlaygroundArrivalDto
import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.data.model.PlaygroundJoinDto
import com.happy.friendogly.data.model.PlaygroundMessageDto
import com.happy.friendogly.data.model.PlaygroundSummaryDto
import com.happy.friendogly.data.source.PlaygroundDataSource
import com.happy.friendogly.remote.api.PlaygroundService
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.PatchPlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PatchPlaygroundMessageRequest
import com.happy.friendogly.remote.model.request.PostPlaygroundRequest
import javax.inject.Inject

class PlaygroundDataSourceImpl
    @Inject
    constructor(private val service: PlaygroundService) : PlaygroundDataSource {
        override suspend fun postPlayground(request: PostPlaygroundRequest): Result<MyPlaygroundDto> {
            val result = runCatching { service.postPlayground(request).data.toData() }

            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                else -> Result.failure(exception)
            }
        }

        override suspend fun patchPlaygroundArrival(request: PatchPlaygroundArrivalRequest): Result<PlaygroundArrivalDto> {
            val result = kotlin.runCatching { service.patchPlaygroundArrival(request).data.toData() }

            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                else -> Result.failure(exception)
            }
        }

        override suspend fun getPetExistence(): Result<PetExistenceDto> {
            return runCatching { service.getPetExistence().data.toData() }
        }

        override suspend fun getNearPlaygrounds(): Result<List<PlaygroundDto>> {
            return runCatching { service.getPlaygrounds().data.toData() }
        }

        override suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfoDto> {
            return runCatching { service.getPlaygroundInfo(id).data.toData() }
        }

        override suspend fun getPlaygroundSummary(playgroundId: Long): Result<PlaygroundSummaryDto> {
            return runCatching { service.getPlaygroundSummary(playgroundId).data.toData() }
        }

        override suspend fun postPlaygroundJoin(playgroundId: Long): Result<PlaygroundJoinDto> {
            val result = runCatching { service.postPlaygroundJoin(playgroundId).data.toData() }

            return when (val exception = result.exceptionOrNull()) {
                null -> result
                is ApiExceptionResponse -> Result.failure(exception.toData())
                else -> Result.failure(exception)
            }
        }

        override suspend fun deletePlaygroundLeave(): Result<Unit> {
            return runCatching { service.deletePlaygroundLeave() }
        }

        override suspend fun patchPlaygroundMessage(request: PatchPlaygroundMessageRequest): Result<PlaygroundMessageDto> {
            return runCatching { service.patchPlaygroundMessage(request).data.toData() }
        }
    }
