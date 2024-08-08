package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MyFootprintDto
import com.happy.friendogly.presentation.ui.woof.model.MyFootprint

fun MyFootprintDto.toDomain(): MyFootprint {
    return MyFootprint(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
    )
}
