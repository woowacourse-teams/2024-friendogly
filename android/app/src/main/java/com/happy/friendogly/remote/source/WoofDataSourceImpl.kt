package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.FootprintDto
import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.data.model.FootprintMarkBtnInfoDto
import com.happy.friendogly.data.model.MyFootprintDto
import com.happy.friendogly.data.model.WalkStatusDto
import com.happy.friendogly.data.source.WoofDataSource
import com.happy.friendogly.remote.api.WoofService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.request.FootprintRequest
import com.happy.friendogly.remote.model.request.WalkStatusRequest

class WoofDataSourceImpl(private val service: WoofService) : WoofDataSource {
    override suspend fun postFootprint(request: FootprintRequest): Result<MyFootprintDto> {
        return runCatching { service.postFootprint(request).data.toData() }
    }

    override suspend fun patchWalkStatus(request: WalkStatusRequest): Result<WalkStatusDto> {
        return runCatching { service.patchWalkStatus(request).data.toData() }
    }

    override suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfoDto> {
        return runCatching {
            service.getFootprintMarkBtnInfo().data.toData()
        }
    }

    override suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootprintDto>> {
        return runCatching {
            service.getNearFootprints(latitude, longitude).data.toData()
        }
    }

    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto> {
        return runCatching {
            service.getFootprintInfo(footprintId).data.toData()
        }
    }
}
