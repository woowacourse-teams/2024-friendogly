package com.happy.friendogly.data.model

data class PlaygroundSummaryDto(
    val id: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val petImageUrls: List<String>,
)
