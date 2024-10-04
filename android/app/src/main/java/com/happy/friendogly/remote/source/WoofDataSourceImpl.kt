package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.data.model.PlaygroundArrivalDto
import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.remote.api.WoofService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.PlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PlaygroundRequest
import javax.inject.Inject

class WoofDataSourceImpl
    @Inject
    constructor(private val service: WoofService) : WoofDataSource {
        override suspend fun postPlayground(request: PlaygroundRequest): Result<MyPlaygroundDto> {
            return runCatching { service.postFootprint(request).data.toData() }
        }

        override suspend fun patchPlaygroundArrival(request: PlaygroundArrivalRequest): Result<PlaygroundArrivalDto> {
            return runCatching { service.patchPlaygroundArrival(request).data.toData() }
        }

        override suspend fun getFootprintMarkBtnInfo(): Result<PetExistenceDto> {
            return runCatching { service.getPetExistence().data.toData() }
        }

        override suspend fun getNearFootprints(): Result<List<PlaygroundDto>> {
            return runCatching { service.getPlaygrounds().data.toData() }
        }

        override suspend fun getPlaygroundInfo(id: Long): Result<PlaygroundInfoDto> {
            return runCatching { service.getPlaygroundInfo(id).data.toData() }
        }

        override suspend fun deleteFootprint(footprintId: Long): Result<Unit> {
            return runCatching { service.deleteFootprint(footprintId) }
        }
    }
