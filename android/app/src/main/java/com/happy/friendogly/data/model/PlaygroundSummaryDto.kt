package com.happy.friendogly.data.model

data class PlaygroundSummaryDto(
    val playgroundId: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val petImageUrls: List<String>,
)
