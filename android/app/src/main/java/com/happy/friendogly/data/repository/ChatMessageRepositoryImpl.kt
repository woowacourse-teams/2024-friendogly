package com.happy.friendogly.data.repository

import androidx.datastore.dataStore
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.source.ChatMessageDataSource
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.domain.repository.ChatMessageRepository
import com.happy.friendogly.local.mapper.toData
import com.happy.friendogly.local.room.ChatRoomDao
import java.time.LocalDateTime
import javax.inject.Inject
class ChatMessageRepositoryImpl @Inject constructor(
    private val dataSource: ChatMessageDataSource,
    private val chatRoomDao: ChatRoomDao
) : ChatMessageRepository {
    override suspend fun getAllChatMessages(
        myMemberId: Long,
        chatRoomId: Long
    ): Result<List<ChatComponent>> {
        val chatMessages = dataSource.getAllChatMessages(chatRoomId).mapCatching { it.toDomain(myMemberId) }
        chatRoomDao.addMessagesToChatRoom(chatRoomId, chatMessages.getOrThrow().toData())
        return chatMessages
    }

    override suspend fun getChatMessageByTime(
        myMemberId: Long,
        chatRoomId: Long,
        since: LocalDateTime,
        until: LocalDateTime
    ): Result<List<ChatComponent>> =
        dataSource.getChatMessagesByTime(chatRoomId, since, until).mapCatching {
            it.toDomain(myMemberId)
        }

}

