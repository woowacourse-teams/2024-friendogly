package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toEnter
import com.happy.friendogly.data.mapper.toLeave
import com.happy.friendogly.data.mapper.toMine
import com.happy.friendogly.data.mapper.toOther
import com.happy.friendogly.data.model.MessageTypeDto
import com.happy.friendogly.data.source.WebSocketDataSource
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WebSocketRepositoryImpl
    @Inject
    constructor(private val source: WebSocketDataSource) : WebSocketRepository {
        override suspend fun connect(): Result<Unit> = source.connect()

        override suspend fun disconnect(): Result<Unit> = source.disconnect()

        override suspend fun publishEnter(memberId: Long) = source.publishEnter(memberId)

        override suspend fun publishSend(
            chatRoomId: Long,
            content: String,
        ) = source.publishSend(chatRoomId, content)

        override suspend fun publishLeave(chatRoomId: Long) = source.publishLeave(chatRoomId)

        override suspend fun subscribeMessage(
            chatRoomId: Long,
            myMemberId: Long,
        ): Flow<ChatComponent> =
            source.subscribeMessage(chatRoomId).map {
                when (it.messageType) {
                    MessageTypeDto.ENTER -> it.toEnter()
                    MessageTypeDto.LEAVE -> it.toLeave()
                    MessageTypeDto.CHAT -> {
                        if (myMemberId == it.senderMemberId) it.toMine() else it.toOther()
                    }
                }
            }
    }
