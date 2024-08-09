package com.happy.friendogly.domain.model

data class ChatRoom(
    val chatRoomId: Long,
    val clubName: String,
    val memberCount: Int,
    val clubImageUrl: String? = null,
)
