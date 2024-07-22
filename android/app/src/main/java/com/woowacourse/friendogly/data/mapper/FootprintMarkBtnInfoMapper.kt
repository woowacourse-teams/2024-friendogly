package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.presentation.utils.parseToLocalDateTime

fun FootprintMarkBtnInfoDto.toDomain(): FootprintMarkBtnInfo {
    return FootprintMarkBtnInfo(latestCreatedAt = parseToLocalDateTime(createdAt))
}
