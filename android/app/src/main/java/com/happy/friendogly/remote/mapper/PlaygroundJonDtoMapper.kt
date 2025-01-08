package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundJoinDto
import com.happy.friendogly.remote.model.response.PlaygroundJoinResponse

fun PlaygroundJoinResponse.toData(): PlaygroundJoinDto {
    return PlaygroundJoinDto(
        playgroundId = playgroundId,
        memberId = memberId,
        message = message,
        isArrived = isArrived,
        exitTime = exitTime,
    )
}
