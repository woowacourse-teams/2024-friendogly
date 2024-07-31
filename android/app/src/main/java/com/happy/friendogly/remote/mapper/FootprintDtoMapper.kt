package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.FootprintDto
import com.happy.friendogly.remote.model.response.FootprintsNearResponse

fun FootprintsNearResponse.toData(): FootprintDto {
    return FootprintDto(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
    )
}

fun List<FootprintsNearResponse>.toData(): List<FootprintDto> {
    return map { response ->
        response.toData()
    }
}
