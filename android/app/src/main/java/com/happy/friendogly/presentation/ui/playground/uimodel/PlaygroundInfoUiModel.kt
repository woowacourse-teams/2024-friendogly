package com.happy.friendogly.presentation.ui.playground.uimodel

data class PlaygroundInfoUiModel(
    val id: Long,
    val totalPetCount: Int,
    val arrivedPetCount: Int,
    val isParticipating: Boolean,
    val petDetails: List<PlaygroundPetDetailUiModel>,
)
