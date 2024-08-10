package com.happy.friendogly.presentation.ui.profilesetting.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name: String,
    val imageUrl: String?,
)
