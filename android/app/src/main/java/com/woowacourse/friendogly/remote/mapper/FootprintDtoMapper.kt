package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.remote.model.response.FootprintResponse

fun FootprintResponse.toData(): FootprintDto {
    return FootprintDto(
        footprintId = this.footprintId,
        latitude = this.latitude,
        longitude = this.longitude,
        createdAt = this.createdAt,
        isMine = this.isMine,
    )
}

fun List<FootprintResponse>.toData(): List<FootprintDto> {
    return map { response ->
        response.toData()
    }
}
