package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.LocationDto
import com.happy.friendogly.remote.model.response.LocationResponse

fun LocationResponse.toData(): LocationDto {
    return LocationDto(
        latitude = this.latitude,
        longitude = this.longitude,
    )
}
