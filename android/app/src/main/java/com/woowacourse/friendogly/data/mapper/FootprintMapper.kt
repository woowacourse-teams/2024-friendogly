package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.domain.model.Footprint

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
