package com.woowacourse.friendogly.remote.source

import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.data.model.LandMarkDto

interface WoofDataSource {
    suspend fun getNearFootPrints(latLng: LatLng): Result<List<FootPrintDto>>

    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
