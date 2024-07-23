package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.presentation.utils.parseToLocalDateTimeOrNull
import com.woowacourse.friendogly.remote.model.response.FootprintMarkBtnInfoResponse

fun FootprintMarkBtnInfoDto.toDomain(): FootprintMarkBtnInfo {
    return FootprintMarkBtnInfo(createdAt = parseToLocalDateTimeOrNull(createdAt))
}

fun FootprintMarkBtnInfoResponse.toData(): FootprintMarkBtnInfoDto {
    return FootprintMarkBtnInfoDto(
        createdAt = createdAt,
    )
}
