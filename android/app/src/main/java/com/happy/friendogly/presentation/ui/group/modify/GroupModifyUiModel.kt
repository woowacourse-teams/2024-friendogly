package com.happy.friendogly.presentation.ui.group.modify

import kotlinx.serialization.Serializable

@Serializable
data class GroupModifyUiModel(
    val title: String,
    val content: String,
    val groupPoster: String?,
)
