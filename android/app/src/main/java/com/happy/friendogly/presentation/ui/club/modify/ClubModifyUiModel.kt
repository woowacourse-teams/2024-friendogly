package com.happy.friendogly.presentation.ui.club.modify

import com.happy.friendogly.domain.model.ClubState
import kotlinx.serialization.Serializable

@Serializable
data class ClubModifyUiModel(
    val title: String,
    val content: String,
    val clubState: ClubState,
)
