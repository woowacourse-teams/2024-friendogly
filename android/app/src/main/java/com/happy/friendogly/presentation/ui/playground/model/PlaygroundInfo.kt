package com.happy.friendogly.presentation.ui.playground.model

data class PlaygroundInfo(
    val id: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val isParticipating: Boolean,
    val playgroundPetDetails: List<PlaygroundPetDetail>,
)
