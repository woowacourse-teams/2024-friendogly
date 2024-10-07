package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundSummaryResponse(
    val id: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
//    val petImageUrls: List<String>
)
