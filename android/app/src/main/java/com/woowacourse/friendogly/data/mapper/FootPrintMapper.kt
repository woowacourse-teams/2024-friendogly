package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.domain.model.FootPrint

fun FootPrintDto.toDomain(): FootPrint {
    return FootPrint(
        footPrintId = footPrintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
    )
}

fun List<FootPrintDto>.toDomain(): List<FootPrint> {
    return map { dto ->
        dto.toDomain()
    }
}
