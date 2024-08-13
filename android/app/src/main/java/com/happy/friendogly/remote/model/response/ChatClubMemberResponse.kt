package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatClubMemberResponse(
    val isOwner: Boolean,
    val memberId: Long,
    val memberName: String,
    val memberProfileImageUrl: String,
)
