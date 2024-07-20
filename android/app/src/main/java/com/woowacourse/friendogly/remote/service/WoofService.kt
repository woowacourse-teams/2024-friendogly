package com.woowacourse.friendogly.remote.service

import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.data.model.FootPrintInfoDto
import com.woowacourse.friendogly.data.model.LandMarkDto
import retrofit2.http.GET

interface WoofService {
    @GET("")
    suspend fun getNearFootPrints(latLng: LatLng): Result<List<FootPrintInfoDto>>

    @GET("")
    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
