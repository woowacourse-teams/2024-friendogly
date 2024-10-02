package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.remote.model.response.PlaygroundResponse

fun PlaygroundResponse.toData(): PlaygroundDto {
    return PlaygroundDto(
        id = id,
        latitude = latitude,
        longitude = longitude,
        isParticipating = isParticipating,
    )
}

fun List<PlaygroundResponse>.toData(): List<PlaygroundDto> {
    return map { response ->
        response.toData()
    }
}
