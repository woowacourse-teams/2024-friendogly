package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.response.BaseResponse
import com.woowacourse.friendogly.remote.model.response.FootprintInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FootprintService {
    @GET(ApiClient.Footprints.GET_FOOTPRINT_INFO)
    suspend fun getFootprintInfo(
        @Path("footprintId") footprintId: Long,
    ): BaseResponse<FootprintInfoResponse>
}
