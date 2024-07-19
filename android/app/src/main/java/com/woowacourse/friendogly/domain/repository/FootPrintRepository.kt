package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.presentation.ui.woof.footprint.FootPrintInfo

interface FootPrintRepository {
    suspend fun getFootPrintInfo(memberId: Long): Result<FootPrintInfo>
}
