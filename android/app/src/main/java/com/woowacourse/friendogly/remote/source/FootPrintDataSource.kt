package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootPrintDto

interface FootPrintDataSource {
    suspend fun getFootPrint(memberId: Long): Result<FootPrintDto>
}
