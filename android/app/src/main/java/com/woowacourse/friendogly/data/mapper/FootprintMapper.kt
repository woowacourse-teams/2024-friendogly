package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintDto
import com.woowacourse.friendogly.domain.model.Footprint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun FootprintDto.toDomain(): Footprint {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(createdAt, formatter)
    return Footprint(
        footPrintId = footPrintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = localDateTime,
        isMine = isMine,
        imageUrl = imageUrl,
    )
}

fun List<FootprintDto>.toDomain(): List<Footprint> {
    return map { dto ->
        dto.toDomain()
    }
}
