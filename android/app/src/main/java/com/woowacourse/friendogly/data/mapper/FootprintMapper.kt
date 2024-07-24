package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.remote.model.response.FootprintsNearResponse

fun FootprintDto.toDomain(): Footprint {
    return Footprint(
        footprintId = footPrintId,
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

fun FootprintsNearResponse.toData(): FootprintDto {
    return FootprintDto(
        footPrintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
        imageUrl = imageUrl,
    )
}

fun List<FootprintsNearResponse>.toData(): List<FootprintDto> {
    return map { response ->
        response.toData()
    }
}
