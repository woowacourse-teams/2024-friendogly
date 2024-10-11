package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundSummaryDto
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary

fun PlaygroundSummaryDto.toDomain(): PlaygroundSummary {
    return PlaygroundSummary(
        playgroundId = playgroundId,
        totalPetCount = totalPetCount,
        arrivedPetCount = arrivedPetCount,
        petImageUrls = petImageUrls,
    )
}
