package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.source.WebSocketDataSource
import com.happy.friendogly.remote.api.WebSocketService
import com.happy.friendogly.remote.mapper.toData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WebSocketDataSourceImpl(private val service: WebSocketService) : WebSocketDataSource {
    override suspend fun publishInvite(chatRoomId: Long) = service.publishInvite(chatRoomId)

    override suspend fun publishSend(
        chatRoomId: Long,
        content: String,
    ) = service.publishSend(chatRoomId, content)

    override suspend fun publishLeave(chatRoomId: Long) = service.publishLeave(chatRoomId)

    override suspend fun subscribeMessage(chatRoomId: Long): Flow<MessageDto> = service.subscribeMessage(chatRoomId).map { it.toData() }
}
