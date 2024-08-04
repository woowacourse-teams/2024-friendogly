package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubDetailMemberResponse(
    val id: Long,
    val name : String,
    val imageUrl: String,
)
