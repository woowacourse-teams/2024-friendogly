package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MessageDto
import java.time.LocalDateTime

interface ChatMessageDataSource {
    suspend fun getAllChatMessages(chatRoomId: Long): Result<List<MessageDto>>

    suspend fun getChatMessagesByTime(
        chatRoomId: Long,
        since: LocalDateTime,
        until: LocalDateTime,
    ): Result<List<MessageDto>>
}
