package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.ChatComponent
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    suspend fun publishInvite(chatRoomId: Long)

    suspend fun publishSend(chatRoomId: Long, content: String)

    suspend fun publishLeave(chatRoomId: Long)

    suspend fun subscribeMessage(chatRoomId: Long): Flow<ChatComponent>
}
