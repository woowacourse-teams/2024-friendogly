package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.MyFootprintDto
import com.happy.friendogly.remote.model.response.MyFootprintResponse

fun MyFootprintResponse.toData(): MyFootprintDto {
    return MyFootprintDto(
        footprintId = id,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
    )
}
