package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootPrintMineLatestDto
import com.woowacourse.friendogly.remote.model.response.FootPrintMineLatestResponse

fun FootPrintMineLatestResponse.toData(): FootPrintMineLatestDto {
    return FootPrintMineLatestDto(
        createdAt = createdAt,
    )
}
