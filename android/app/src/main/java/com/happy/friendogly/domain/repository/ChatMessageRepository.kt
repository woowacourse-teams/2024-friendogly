package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.ChatComponent
import java.time.LocalDateTime

interface ChatMessageRepository {
    suspend fun getAllChatMessages(
        myMemberId: Long,
        chatRoomId: Long,
    ): Result<List<ChatComponent>>

    suspend fun getChatMessageByTime(
        myMemberId: Long,
        chatRoomId: Long,
        since: LocalDateTime,
        until: LocalDateTime,
    ): Result<List<ChatComponent>>
}
