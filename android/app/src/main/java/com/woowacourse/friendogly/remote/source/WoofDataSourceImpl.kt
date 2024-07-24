package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.data.model.FootprintSaveDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.data.source.WoofDataSource
import com.woowacourse.friendogly.remote.api.WoofService
import com.woowacourse.friendogly.remote.mapper.toData
import com.woowacourse.friendogly.remote.model.request.FootprintRequest

class WoofDataSourceImpl(private val service: WoofService) : WoofDataSource {
    override suspend fun postFootprint(request: FootprintRequest): Result<FootprintSaveDto> {
        return runCatching { service.postFootprint(request).data.toData() }
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

    override suspend fun getLandMarks(): Result<List<LandMarkDto>> {
        return Result.success(listOf(LandMarkDto()))
//        return woofService.data.getLandMarks()
    }
}
