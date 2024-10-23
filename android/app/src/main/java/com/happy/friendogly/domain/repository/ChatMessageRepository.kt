package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.ChatComponent
import kotlinx.coroutines.flow.Flow

interface ChatMessageRepository {
    suspend fun getChatMessagesInRange(
        myMemberId: Long,
        chatRoomId: Long,
        offset: Int,
        limit: Int,
    ): Flow<List<ChatComponent>>

    suspend fun saveMessage(
        chatRoomId: Long,
        message: ChatComponent,
    ): Flow<Unit>
}
