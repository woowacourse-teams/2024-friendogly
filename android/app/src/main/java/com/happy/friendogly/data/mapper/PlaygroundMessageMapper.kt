package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundMessageDto
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundMessage

fun PlaygroundMessageDto.toDomain(): PlaygroundMessage {
    return PlaygroundMessage(
        message = message,
    )
}
