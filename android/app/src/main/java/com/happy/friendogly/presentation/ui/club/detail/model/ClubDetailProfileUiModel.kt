package com.happy.friendogly.presentation.ui.club.detail.model

data class ClubDetailProfileUiModel(
    val id: Long,
    val name: String,
    val imageUrl: String? = null,
    val isPet: Boolean,
    val isMyPet: Boolean,
)
