package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintSaveDto
import com.happy.friendogly.domain.model.FootprintSave

fun FootprintSaveDto.toDomain(): FootprintSave {
    return FootprintSave(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
    )
}
