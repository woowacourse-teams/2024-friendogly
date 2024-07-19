package com.woowacourse.friendogly.domain.repository

import com.woowacourse.friendogly.presentation.ui.woof.footprint.FootPrint

interface FootPrintRepository {
    suspend fun getFootPrint(memberId: Long): Result<FootPrint>
}
