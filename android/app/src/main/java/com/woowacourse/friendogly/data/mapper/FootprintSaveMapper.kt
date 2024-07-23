package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintSaveDto
import com.woowacourse.friendogly.domain.model.FootprintSave
import com.woowacourse.friendogly.remote.model.response.FootprintSaveResponse

fun FootprintSaveDto.toDomain(): FootprintSave {
    return FootprintSave(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
    )
}

fun FootprintSaveResponse.toData(): FootprintSaveDto {
    return FootprintSaveDto(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
    )
}
