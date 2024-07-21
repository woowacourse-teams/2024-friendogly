package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.request.FootPrintRequest
import com.woowacourse.friendogly.remote.model.response.FootPrintResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WoofService {
    @POST("/footprints")
    suspend fun postFootPrint(
        @Body footPrintRequest: FootPrintRequest,
    )

    @GET("/footprints/near")
    suspend fun getNearFootPrints(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): List<FootPrintResponse>

    @GET("")
    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
