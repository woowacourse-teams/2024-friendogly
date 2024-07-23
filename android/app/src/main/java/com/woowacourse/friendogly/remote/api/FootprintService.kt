package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.data.model.FootprintInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FootprintService {
    @GET(ApiClient.Footprints.GET_FOOTPRINT_INFO)
    suspend fun getFootprintInfo(
        @Path("footprintId") footprintId: Long,
    ): FootprintInfoDto
}
