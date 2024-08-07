package com.happy.friendogly.data.model

import java.time.LocalDateTime

data class MessageDto(
    val messageType: MessageTypeDto,
    val senderMemberId: Long,
    val senderName: String,
    val content: String?,
    val createdAt: LocalDateTime,
    val profilePictureUrl: String,
)
