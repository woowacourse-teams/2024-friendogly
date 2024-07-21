package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.LocationDto
import com.woowacourse.friendogly.domain.model.Location

fun LocationDto.toDomain(): Location {
    return Location(
        latitude = this.latitude,
        longitude = this.longitude,
    )
}
