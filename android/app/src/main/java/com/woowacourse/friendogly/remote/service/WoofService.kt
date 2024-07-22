package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.remote.model.request.FootprintRequest
import com.woowacourse.friendogly.remote.model.response.FootprintMarkBtnInfoResponse
import com.woowacourse.friendogly.remote.model.response.FootprintsNearResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WoofService {
    @POST("/footprints")
    suspend fun postFootPrint(
        @Body request: FootprintRequest,
    )

    @GET("/footprints/mine/latest")
    suspend fun getFootPrintMarkBtnInfo(): FootprintMarkBtnInfoResponse

    @GET("/footprints/near")
    suspend fun getNearFootPrints(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): List<FootprintsNearResponse>

    @GET("")
    suspend fun getLandMarks(): Result<List<LandMarkDto>>
}
