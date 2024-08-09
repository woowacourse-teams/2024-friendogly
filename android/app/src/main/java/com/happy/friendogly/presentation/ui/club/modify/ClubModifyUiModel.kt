package com.happy.friendogly.presentation.ui.club.modify

import kotlinx.serialization.Serializable

@Serializable
data class ClubModifyUiModel(
    val title: String,
    val content: String,
    val clubPoster: String?,
)
