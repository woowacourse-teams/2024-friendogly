package com.happy.friendogly.data.model

data class PlaygroundInfoDto(
    val id: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val isParticipating: Boolean,
    val playgroundPetDetails: List<PlaygroundPetDetailDto>,
)
