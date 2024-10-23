package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundMessageDto
import com.happy.friendogly.remote.model.PlaygroundMessageResponse

fun PlaygroundMessageResponse.toData(): PlaygroundMessageDto {
    return PlaygroundMessageDto(
        message = message,
    )
}
