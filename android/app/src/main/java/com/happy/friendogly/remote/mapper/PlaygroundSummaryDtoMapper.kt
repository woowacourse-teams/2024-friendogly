package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundSummaryDto
import com.happy.friendogly.remote.model.response.PlaygroundSummaryResponse

fun PlaygroundSummaryResponse.toData(): PlaygroundSummaryDto {
    return PlaygroundSummaryDto(
        playgroundId = playgroundId,
        totalPetCount = totalPetCount,
        arrivedPetCount = arrivedPetCount,
        petImageUrls = petImageUrls,
//        petImageUrls =
//            listOf(
//                "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
//                "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
//                "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
//            ),
    )
}
