package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.remote.model.response.PlaygroundInfoResponse

fun PlaygroundInfoResponse.toData(): PlaygroundInfoDto {
    return PlaygroundInfoDto(
        id = id,
        totalPetCount = totalPetCount,
        arrivedPetCount = arrivedPetCount,
        isParticipating = isParticipating,
        playgroundPetDetails = playgroundPetDetails.toData(),
    )
}
