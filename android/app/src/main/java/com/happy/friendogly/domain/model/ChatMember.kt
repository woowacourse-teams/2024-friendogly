package com.happy.friendogly.domain.model

data class ChatMember(
    val isOwner: Boolean,
    val memberId: Long,
    val memberName: String,
    val memberProfileImageUrl: String,
)
