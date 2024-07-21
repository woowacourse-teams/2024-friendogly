package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.request.FootPrintRequest
import com.woowacourse.friendogly.remote.model.response.FootPrintMineLatestResponse
import com.woowacourse.friendogly.remote.model.response.FootPrintsNearResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WoofService {
    @POST("/footprints")
    suspend fun postFootPrint(
        @Body request: FootPrintRequest,
    )

    @GET("/footprints/mine/latest")
    suspend fun getMyLatestFootPrintTime(): FootPrintMineLatestResponse

    @GET("/footprints/near")
    suspend fun getNearFootPrints(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): List<FootPrintsNearResponse>

    @GET("")
    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
