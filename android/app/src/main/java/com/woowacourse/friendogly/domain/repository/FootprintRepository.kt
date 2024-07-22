package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.FootprintInfo

interface FootprintRepository {
    suspend fun getFootPrintInfo(footprintId: Long): Result<FootprintInfo>
}
