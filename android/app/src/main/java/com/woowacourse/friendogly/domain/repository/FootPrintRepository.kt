package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.domain.model.FootPrintInfo

interface FootPrintRepository {
    suspend fun getFootPrintInfo(memberId: Long): Result<FootPrintInfo>
}
