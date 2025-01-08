package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.source.WebSocketDataSource
import com.happy.friendogly.remote.api.WebSocketService
import com.happy.friendogly.remote.mapper.toData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WebSocketDataSourceImpl
    @Inject
    constructor(private val service: WebSocketService) : WebSocketDataSource {
        override suspend fun connect(): Result<Unit> =
            runCatching {
                service.connect()
            }

        override suspend fun disconnect(): Result<Unit> =
            runCatching {
                service.disconnect()
            }

        override suspend fun publishEnter(chatRoomId: Long): Result<Unit> =
            runCatching {
                service.publishInvite(chatRoomId)
            }

        override suspend fun publishSend(
            chatRoomId: Long,
            content: String,
        ): Result<Unit> =
            runCatching {
                service.publishSend(chatRoomId, content)
            }

        override suspend fun publishLeave(chatRoomId: Long): Result<Unit> = runCatching { service.publishLeave(chatRoomId) }

        override suspend fun subscribeMessage(chatRoomId: Long): Flow<MessageDto> = service.subscribeMessage(chatRoomId).map { it.toData() }
    }
