package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundInfoResponse(
    val id: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val isParticipating: Boolean,
    val playgroundPetDetails: List<PlaygroundPetDetailResponse>,
)
