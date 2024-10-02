package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundInfoDto
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo

fun PlaygroundInfoDto.toDomain(): PlaygroundInfo {
    return PlaygroundInfo(
        id = id,
        totalPetCount = totalPetCount,
        arrivedPetCount = arrivedPetCount,
        isParticipating = isParticipating,
        playgroundPetDetails = playgroundPetDetails.toDomain(),
    )
}
