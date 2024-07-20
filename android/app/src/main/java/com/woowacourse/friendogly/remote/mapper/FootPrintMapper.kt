package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.remote.model.response.FootPrintResponse

fun List<FootPrintResponse>.toData(): List<FootPrintDto> {
    return map { footPrintResponse ->
        FootPrintDto(
            footPrintId = footPrintResponse.footPrintId,
            latitude = footPrintResponse.latitude,
            longitude = footPrintResponse.longitude,
            createdAt = footPrintResponse.createdAt,
            isMine = footPrintResponse.isMine,
        )
    }
}
