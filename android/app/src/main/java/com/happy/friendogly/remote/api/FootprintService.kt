package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.FootprintInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FootprintService {
    @GET(ApiClient.Footprints.GET_FOOTPRINT_INFO)
    suspend fun getFootprintInfo(
        @Path("footprintId") footprintId: Long,
    ): BaseResponse<FootprintInfoResponse>
}
