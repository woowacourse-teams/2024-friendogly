package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.remote.model.response.FootPrintResponse

fun FootPrintResponse.toData(): FootPrintDto {
    return FootPrintDto(
        footPrintId = footPrintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
    )
}

fun List<FootPrintResponse>.toData(): List<FootPrintDto> {
    return map { response ->
        response.toData()
    }
}
