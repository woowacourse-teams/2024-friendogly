package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubMemberDetailResponse(
    val id: Long,
    val name : String,
    val imageUrl: String,
)
