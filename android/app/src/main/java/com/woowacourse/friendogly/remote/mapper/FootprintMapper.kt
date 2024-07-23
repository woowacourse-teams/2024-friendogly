package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.remote.model.response.FootprintsNearResponse

fun FootprintsNearResponse.toData(): FootprintDto {
    return FootprintDto(
        footPrintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
        imageUrl = imageUrl,
    )
}

fun List<FootprintsNearResponse>.toData(): List<FootprintDto> {
    return map { response ->
        response.toData()
    }
}
