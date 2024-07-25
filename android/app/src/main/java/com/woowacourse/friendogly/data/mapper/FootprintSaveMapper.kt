package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintSaveDto
import com.woowacourse.friendogly.domain.model.FootprintSave

fun FootprintSaveDto.toDomain(): FootprintSave {
    return FootprintSave(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
    )
}
