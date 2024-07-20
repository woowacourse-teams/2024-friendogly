package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.response.FootPrintResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WoofService {
    @GET("/footprints/near")
    suspend fun getNearFootPrints(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): List<FootPrintResponse>

    @GET("")
    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
