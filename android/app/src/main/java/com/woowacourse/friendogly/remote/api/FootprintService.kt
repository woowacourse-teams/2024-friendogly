package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.request.PostFootprintsRequest
import com.woowacourse.friendogly.remote.model.response.BaseResponse
import com.woowacourse.friendogly.remote.model.response.FootprintResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FootprintService {
    @POST(ApiClient.Footprint.POST_FOOTPRINT)
    suspend fun postFootprint(
        @Body body: PostFootprintsRequest,
    ): BaseResponse<Unit>

    @GET(ApiClient.Footprint.GET_FOOTPRINTS)
    suspend fun getFootprints(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): BaseResponse<List<FootprintResponse>>
}
