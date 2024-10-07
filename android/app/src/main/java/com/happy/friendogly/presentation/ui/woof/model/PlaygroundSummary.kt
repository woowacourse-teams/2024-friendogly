package com.happy.friendogly.presentation.ui.woof.model

data class PlaygroundSummary(
    val id: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val petImageUrls: List<String>,
)
