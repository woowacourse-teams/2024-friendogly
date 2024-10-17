package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundArrivalDto
import com.happy.friendogly.remote.model.response.PlaygroundArrivalResponse

fun PlaygroundArrivalResponse.toData(): PlaygroundArrivalDto {
    return PlaygroundArrivalDto(isArrived = isArrived)
}
