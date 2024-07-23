package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.SizeTypeDto
import com.woowacourse.friendogly.remote.model.response.SizeTypeResponse

fun SizeTypeResponse.toData(): SizeTypeDto {
    return when (this) {
        SizeTypeResponse.SMALL -> SizeTypeDto.SMALL
        SizeTypeResponse.MEDIUM -> SizeTypeDto.MEDIUM
        SizeTypeResponse.LARGE -> SizeTypeDto.LARGE
    }
}
