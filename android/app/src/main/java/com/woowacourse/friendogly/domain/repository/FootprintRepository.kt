package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.FootprintInfo

interface FootprintRepository {
    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfo>
}
