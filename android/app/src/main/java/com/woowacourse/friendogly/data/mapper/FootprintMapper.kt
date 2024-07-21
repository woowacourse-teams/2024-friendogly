package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.domain.model.Footprint

fun FootprintDto.toDomain(): Footprint {
    return Footprint(
        footprintId = this.footprintId,
        latitude = this.latitude,
        longitude = this.longitude,
        createdAt = this.createdAt,
        isMine = this.isMine,
    )
}

fun List<FootprintDto>.toDomain(): List<Footprint> {
    return map { dto ->
        dto.toDomain()
    }
}
