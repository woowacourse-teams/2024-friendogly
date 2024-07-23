package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.FootprintInfoDto

interface FootprintDataSource {
    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto>
}
