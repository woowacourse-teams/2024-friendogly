package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.ChatComponent
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    suspend fun publishEnter(chatRoomId: Long): Result<Unit>

    suspend fun publishSend(
        chatRoomId: Long,
        content: String,
    ): Result<Unit>

    suspend fun publishLeave(chatRoomId: Long): Result<Unit>

    suspend fun subscribeMessage(
        chatRoomId: Long,
        myMemberId: Long,
    ): Flow<ChatComponent>
}
