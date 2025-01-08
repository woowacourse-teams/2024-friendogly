package com.happy.friendogly.domain.model

import java.time.LocalDateTime

data class ChatRoom(
    val chatRoomId: Long,
    val clubName: String,
    val memberCount: Int,
    val clubImageUrl: String? = null,
    val recentMessage: String?,
    val recentMessageCreatedAt: LocalDateTime?,
)
