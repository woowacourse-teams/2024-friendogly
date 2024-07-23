package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.presentation.utils.parseToLocalDateTime

fun FootprintDto.toDomain(): Footprint {
    return Footprint(
        footprintId = footPrintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = parseToLocalDateTime(createdAt),
        isMine = isMine,
        imageUrl = imageUrl,
    )
}

fun List<FootprintDto>.toDomain(): List<Footprint> {
    return map { dto ->
        dto.toDomain()
    }
}
