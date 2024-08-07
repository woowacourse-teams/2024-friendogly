package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MessageDto
import kotlinx.coroutines.flow.Flow

interface WebSocketDataSource {
    suspend fun publishInvite(memberId: Long)

    suspend fun publishSend(chatRoomId: Long, content: String)

    suspend fun publishLeave(chatRoomId: Long)

    suspend fun subscribeMessage(chatRoomId: Long): Flow<MessageDto>
}
