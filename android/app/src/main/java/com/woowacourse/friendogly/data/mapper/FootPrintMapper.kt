package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.domain.model.FootPrint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun FootPrintDto.toDomain(): FootPrint {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(createdAt, formatter)
    return FootPrint(
        footPrintId = footPrintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = localDateTime,
        isMine = isMine,
    )
}

fun List<FootPrintDto>.toDomain(): List<FootPrint> {
    return map { dto ->
        dto.toDomain()
    }
}
