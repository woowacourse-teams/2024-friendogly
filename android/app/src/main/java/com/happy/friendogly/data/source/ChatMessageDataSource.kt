package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MessageDto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ChatMessageDataSource {
    suspend fun getAllChatMessages(chatRoomId: Long): Flow<List<MessageDto>>

    suspend fun getChatMessagesByTime(
        chatRoomId: Long,
        since: LocalDateTime,
        until: LocalDateTime,
    ): Flow<List<MessageDto>>
}
