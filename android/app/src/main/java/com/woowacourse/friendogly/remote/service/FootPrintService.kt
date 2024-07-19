package com.woowacourse.friendogly.remote.service

import com.woowacourse.friendogly.data.model.FootPrintDto
import retrofit2.http.GET

interface FootPrintService {
    @GET()
    suspend fun getFootPrint(memberId: Long): Result<FootPrintDto>
}
