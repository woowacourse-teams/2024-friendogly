package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.remote.model.response.FootprintMarkBtnInfoResponse

fun FootprintMarkBtnInfoResponse.toData(): FootprintMarkBtnInfoDto {
    return FootprintMarkBtnInfoDto(
        createdAt = createdAt,
    )
}
