package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubDetailPetResponse(
    val id: Long,
    val name: String,
    val imageUrl: String? = null,
    val isMine: Boolean,
)
