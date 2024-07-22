package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.FootPrintInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FootPrintService {
    @GET("/footprints/{footprintId}")
    suspend fun getFootPrintInfo(
        @Path("footprintId") footprintId: Long,
    ): Result<FootPrintInfoDto>
}
