package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.FootPrintInfoDto
import retrofit2.http.GET

interface FootPrintService {
    @GET()
    suspend fun getFootPrintInfo(memberId: Long): Result<FootPrintInfoDto>
}
