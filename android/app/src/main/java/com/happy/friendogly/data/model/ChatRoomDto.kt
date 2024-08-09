package com.happy.friendogly.data.model

data class ChatRoomDto(
    val chatRoomId: Long,
    val clubName: String,
    val memberCount: Int,
    val clubImageUrl: String? = null,
)
