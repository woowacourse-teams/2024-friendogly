package com.woowacourse.friendogly.data.source

import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.service.WoofService
import com.woowacourse.friendogly.remote.source.WoofDataSource

class WoofDataSourceImpl(private val woofService: WoofService) : WoofDataSource {
    override suspend fun getNearFootPrints(latLng: LatLng): Result<List<FootPrintDto>> {
        return Result.success(listOf(FootPrintDto()))
//        return woofService.getNearFootPrints(latLng)
    }

    override suspend fun getLandMarks(): Result<List<LandMarkDto>> {
        return Result.success(listOf(LandMarkDto()))
//        return woofService.getLandMarks()
    }
}
