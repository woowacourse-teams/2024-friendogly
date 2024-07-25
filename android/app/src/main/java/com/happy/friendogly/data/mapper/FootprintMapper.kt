package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.FootprintDto
import com.happy.friendogly.domain.model.Footprint

fun FootprintDto.toDomain(): Footprint {
    return Footprint(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
        imageUrl = imageUrl,
    )
}

fun List<FootprintDto>.toDomain(): List<Footprint> {
    return map { dto ->
        dto.toDomain()
    }
}
