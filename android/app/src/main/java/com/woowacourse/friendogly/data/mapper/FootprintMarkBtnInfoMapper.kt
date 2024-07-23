package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.presentation.utils.parseToLocalDateTime
import com.woowacourse.friendogly.remote.model.response.FootprintMarkBtnInfoResponse

fun FootprintMarkBtnInfoDto.toDomain(): FootprintMarkBtnInfo {
    return FootprintMarkBtnInfo(createdAt = parseToLocalDateTime(createdAt))
}

fun FootprintMarkBtnInfoResponse.toData(): FootprintMarkBtnInfoDto {
    return FootprintMarkBtnInfoDto(
        createdAt = createdAt,
    )
}
