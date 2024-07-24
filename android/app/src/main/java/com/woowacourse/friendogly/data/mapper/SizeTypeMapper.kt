package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.SizeTypeDto
import com.woowacourse.friendogly.domain.model.SizeType

fun SizeTypeDto.toDomain(): SizeType {
    return when (this) {
        SizeTypeDto.SMALL -> SizeType.SMALL
        SizeTypeDto.MEDIUM -> SizeType.MEDIUM
        SizeTypeDto.LARGE -> SizeType.LARGE
    }
}

fun SizeType.toData(): SizeTypeDto {
    return when (this) {
        SizeType.SMALL -> SizeTypeDto.SMALL
        SizeType.MEDIUM -> SizeTypeDto.MEDIUM
        SizeType.LARGE -> SizeTypeDto.LARGE
    }
}
