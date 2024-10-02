package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.FootprintMarkBtnInfoDto
import com.happy.friendogly.data.model.FootprintRecentWalkStatusDto
import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.remote.api.WoofService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusAutoRequest
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusManualRequest
import com.happy.friendogly.remote.model.request.FootprintRequest
import javax.inject.Inject

class WoofDataSourceImpl
    @Inject
    constructor(private val service: WoofService) : WoofDataSource {
        override suspend fun postFootprint(request: FootprintRequest): Result<MyPlaygroundDto> {
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

        override suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfoDto> {
            return runCatching { service.getFootprintMarkBtnInfo().data.toData() }
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
