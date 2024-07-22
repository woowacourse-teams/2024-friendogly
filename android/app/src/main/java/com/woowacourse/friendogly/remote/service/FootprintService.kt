package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.FootprintInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FootprintService {
    @GET("/footprints/{footprintId}")
    suspend fun getFootPrintInfo(
        @Path("footprintId") footprintId: Long,
    ): Result<FootprintInfoDto>
}
