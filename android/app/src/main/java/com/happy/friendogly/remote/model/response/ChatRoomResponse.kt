package com.happy.friendogly.remote.model.response

import com.happy.friendogly.remote.util.JavaLocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ChatRoomResponse(
    val chatRoomId: Long,
    val memberCount: Int,
    val title: String,
    val imageUrl: String? = null,
    val recentMessage: String?,
    @Serializable(with = JavaLocalDateTimeSerializer::class)
    val recentMessageCreatedAt: LocalDateTime?,
)
