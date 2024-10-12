package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundSummaryDto
import com.happy.friendogly.remote.model.response.PlaygroundSummaryResponse

fun PlaygroundSummaryResponse.toData(): PlaygroundSummaryDto {
    return PlaygroundSummaryDto(
        playgroundId = playgroundId,
        totalPetCount = totalPetCount,
        arrivedPetCount = arrivedPetCount,
        petImageUrls = petImageUrls,
    )
}
