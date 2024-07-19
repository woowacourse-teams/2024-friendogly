package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootPrintInfoDto

interface FootPrintDataSource {
    suspend fun getFootPrintInfo(memberId: Long): Result<FootPrintInfoDto>
}
