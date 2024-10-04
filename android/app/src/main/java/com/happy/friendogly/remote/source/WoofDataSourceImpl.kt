package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.FootprintRecentWalkStatusDto
import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.remote.api.WoofService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusAutoRequest
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusManualRequest
import com.happy.friendogly.remote.model.request.PlaygroundRequest
import javax.inject.Inject

class WoofDataSourceImpl
    @Inject
    constructor(private val service: WoofService) : WoofDataSource {
        override suspend fun postPlayground(request: PlaygroundRequest): Result<MyPlaygroundDto> {
            return runCatching { service.postFootprint(request).data.toData() }
        }

        override suspend fun patchFootprintRecentWalkStatusAuto(
            request: FootprintRecentWalkStatusAutoRequest,
        ): Result<FootprintRecentWalkStatusDto> {
            return runCatching { service.patchFootprintRecentWalkStatusAuto(request).data.toData() }
        }

        override suspend fun patchFootprintRecentWalkStatusManual(
            request: FootprintRecentWalkStatusManualRequest,
        ): Result<FootprintRecentWalkStatusDto> {
            return runCatching { service.patchFootprintRecentWalkStatusManual(request).data.toData() }
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
