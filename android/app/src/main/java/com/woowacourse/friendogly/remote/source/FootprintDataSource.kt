package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootprintInfoDto

interface FootprintDataSource {
    suspend fun getFootPrintInfo(footprintId: Long): Result<FootprintInfoDto>
}
