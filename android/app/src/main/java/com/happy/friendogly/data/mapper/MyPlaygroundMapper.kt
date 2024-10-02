package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MyPlaygroundDto
import com.happy.friendogly.presentation.ui.woof.model.MyPlayground
import com.happy.friendogly.presentation.ui.woof.model.Playground

fun MyPlaygroundDto.toDomain(): MyPlayground {
    return MyPlayground(
        id = id,
        latitude = latitude,
        longitude = longitude,
    )
}

fun MyPlayground.toPlayground(): Playground {
    return Playground(
        id = id,
        latitude = latitude,
        longitude = longitude,
        isParticipated = true,
    )
}
