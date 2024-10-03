package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.source.ChatMessageDataSource
import com.happy.friendogly.remote.api.ChatMessageService
import com.happy.friendogly.remote.mapper.toData
import java.time.LocalDateTime
import javax.inject.Inject

class ChatMessageDataSourceImpl
    @Inject
    constructor(
        private val service: ChatMessageService,
    ) : ChatMessageDataSource {
        override suspend fun getAllChatMessages(chatRoomId: Long): Result<List<MessageDto>> =
            runCatching {
                service.getAllChatMessages(chatRoomId).data.map { it.toData() }
            }

        override suspend fun getChatMessagesByTime(
            chatRoomId: Long,
            since: LocalDateTime,
            until: LocalDateTime,
        ): Result<List<MessageDto>> =
            runCatching {
                service.getChatMessagesByTime(
                    chatRoomId,
                    since,
                    until,
                ).data.map { it.toData() }
            }
    }
