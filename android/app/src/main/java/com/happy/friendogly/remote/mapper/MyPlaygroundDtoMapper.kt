package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.remote.model.response.MyPlaygroundResponse

fun MyPlaygroundResponse.toData(): MyPlaygroundDto {
    return MyPlaygroundDto(
        id = id,
        latitude = latitude,
        longitude = longitude,
    )
}
