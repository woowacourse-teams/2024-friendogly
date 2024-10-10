package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundSummaryResponse(
    val playgroundId: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val petImageUrls: List<String>,
)
