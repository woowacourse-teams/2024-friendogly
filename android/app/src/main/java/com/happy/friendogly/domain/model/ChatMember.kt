package com.happy.friendogly.domain.model

data class ChatMember(
    val isOwner: Boolean = false,
    val id: Long,
    val name: String,
    val profileImageUrl: String,
)
