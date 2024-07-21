package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootPrintMarkBtnInfoDto
import com.woowacourse.friendogly.remote.model.response.FootPrintMarkBtnInfoResponse

fun FootPrintMarkBtnInfoResponse.toData(): FootPrintMarkBtnInfoDto {
    return FootPrintMarkBtnInfoDto(
        createdAt = createdAt,
    )
}
