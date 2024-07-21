package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.LocationDto
import com.woowacourse.friendogly.remote.model.response.LocationResponse

fun LocationResponse.toData(): LocationDto {
    return LocationDto(
        latitude = this.latitude,
        longitude = this.longitude,
    )
}
