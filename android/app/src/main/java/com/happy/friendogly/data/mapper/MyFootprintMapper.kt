package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MyFootprintDto
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.MyFootprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus

fun MyFootprintDto.toDomain(): MyFootprint {
    return MyFootprint(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
    )
}

fun MyFootprint.toFootprint(): Footprint {
    return Footprint(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        walkStatus = WalkStatus.BEFORE,
        createdAt = createdAt,
        isMine = true,
    )
}
