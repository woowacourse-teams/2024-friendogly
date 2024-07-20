package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.LandMarkDto
import com.woowacourse.friendogly.domain.model.LandMark

fun List<LandMarkDto>.toDomain(): List<LandMark> {
    return map { landMarkDto ->
        LandMark()
    }
}
