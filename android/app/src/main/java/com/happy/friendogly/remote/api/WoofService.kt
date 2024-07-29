package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.FootprintRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.FootprintMarkBtnInfoResponse
import com.happy.friendogly.remote.model.response.FootprintSaveResponse
import com.happy.friendogly.remote.model.response.FootprintsNearResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WoofService {
    @POST(ApiClient.Footprints.POST_FOOTPRINT)
    suspend fun postFootprint(
        @Body request: FootprintRequest,
    ): BaseResponse<FootprintSaveResponse>

    @GET(ApiClient.Footprints.GET_FOOTPRINTS_NEAR)
    suspend fun getNearFootprints(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): BaseResponse<List<FootprintsNearResponse>>

    @GET(ApiClient.Footprints.GET_FOOTPRINT_MINE_LATEST)
    suspend fun getFootprintMarkBtnInfo(): BaseResponse<FootprintMarkBtnInfoResponse>
}
