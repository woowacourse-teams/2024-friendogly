package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootprintSaveDto
import com.woowacourse.friendogly.remote.model.response.FootprintSaveResponse

fun FootprintSaveResponse.toData(): FootprintSaveDto {
    return FootprintSaveDto(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
    )
}
