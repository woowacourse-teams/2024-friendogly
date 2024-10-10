package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.source.ChatMessageDataSource
import com.happy.friendogly.remote.api.ChatMessageService
import com.happy.friendogly.remote.mapper.toData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class ChatMessageDataSourceImpl
    @Inject
    constructor(
        private val service: ChatMessageService,
    ) : ChatMessageDataSource {
        override suspend fun getAllChatMessages(chatRoomId: Long): Flow<List<MessageDto>> =
            flow {
                emit(service.getAllChatMessages(chatRoomId).data.map { it.toData() })
            }

        override suspend fun getChatMessagesByTime(
            chatRoomId: Long,
            since: LocalDateTime,
            until: LocalDateTime,
        ): Flow<List<MessageDto>> =
            flow {
                emit(
                    service.getChatMessagesByTime(
                        chatRoomId,
                        since,
                        until,
                    ).data.map { it.toData() },
                )
            }
    }
