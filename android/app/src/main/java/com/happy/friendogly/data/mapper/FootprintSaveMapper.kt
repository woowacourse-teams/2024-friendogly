package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintSaveDto
import com.happy.friendogly.presentation.ui.woof.model.FootprintSave

fun FootprintSaveDto.toDomain(): FootprintSave {
    return FootprintSave(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
    )
}
