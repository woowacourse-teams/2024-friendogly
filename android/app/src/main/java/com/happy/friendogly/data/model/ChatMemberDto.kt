package com.happy.friendogly.data.model

data class ChatMemberDto(
    val isOwner: Boolean,
    val memberId: Long,
    val memberName: String,
    val memberProfileImageUrl: String,
)
