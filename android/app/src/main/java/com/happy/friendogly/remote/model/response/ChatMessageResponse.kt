package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ChatMessageResponse(
    val messageType: MessageTypeResponse,
    val senderMemberId: Long,
    val senderName: String,
    val content: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val profilePictureUrl: String,
)
