package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintDto
import com.happy.friendogly.presentation.ui.woof.model.Footprint

fun FootprintDto.toDomain(): Footprint {
    return Footprint(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        walkStatus = walkStatus.toDomain(),
        createdAt = createdAt,
        isMine = isMine,
    )
}

fun List<FootprintDto>.toDomain(): List<Footprint> {
    return map { dto ->
        dto.toDomain()
    }
}
