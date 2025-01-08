package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.presentation.ui.playground.model.MyPlayground

fun MyPlaygroundDto.toDomain(): MyPlayground {
    return MyPlayground(
        id = id,
        latitude = latitude,
        longitude = longitude,
    )
}
