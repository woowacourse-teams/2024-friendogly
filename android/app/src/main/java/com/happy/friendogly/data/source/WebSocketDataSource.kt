package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.MessageDto
import kotlinx.coroutines.flow.Flow

interface WebSocketDataSource {

    suspend fun connect():Result<Unit>

    suspend fun disconnect():Result<Unit>

    suspend fun publishEnter(memberId: Long): Result<Unit>

    suspend fun publishSend(
        chatRoomId: Long,
        content: String,
    ): Result<Unit>

    suspend fun publishLeave(chatRoomId: Long): Result<Unit>

    suspend fun subscribeMessage(chatRoomId: Long): Flow<MessageDto>
}
