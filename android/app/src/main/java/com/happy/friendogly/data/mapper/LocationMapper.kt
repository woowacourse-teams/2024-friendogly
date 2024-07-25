package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.LocationDto
import com.happy.friendogly.domain.model.Location

fun LocationDto.toDomain(): Location {
    return Location(
        latitude = this.latitude,
        longitude = this.longitude,
    )
}
