package com.happy.friendogly.data.model

import java.time.LocalDateTime

data class ChatRoomDto(
    val chatRoomId: Long,
    val clubName: String,
    val memberCount: Int,
    val clubImageUrl: String? = null,
    val recentMessage: String?,
    val recentMessageCreatedAt: LocalDateTime?,
)
