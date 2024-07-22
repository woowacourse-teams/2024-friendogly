package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.data.model.FootPrintMarkBtnInfoDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.mapper.toData
import com.woowacourse.friendogly.remote.model.request.FootPrintRequest
import com.woowacourse.friendogly.remote.service.WoofService
import com.woowacourse.friendogly.remote.source.WoofDataSource

class WoofDataSourceImpl(private val woofService: WoofService) : WoofDataSource {
    override suspend fun postFootPrint(request: FootPrintRequest): Result<Unit> {
        return runCatching { woofService.postFootPrint(request) }
    }

    override suspend fun getFootPrintMarkBtnInfo(): Result<FootPrintMarkBtnInfoDto> {
        return runCatching { woofService.getFootPrintMarkBtnInfo().toData() }
    }

    override suspend fun getNearFootPrints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootPrintDto>> {
        return runCatching {
            woofService.getNearFootPrints(latitude, longitude).toData()
        }
    }

    override suspend fun getLandMarks(): Result<List<LandMarkDto>> {
        return Result.success(listOf(LandMarkDto()))
//        return woofService.getLandMarks()
    }
}
