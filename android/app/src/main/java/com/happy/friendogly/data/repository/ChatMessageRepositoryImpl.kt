package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.mapper.toLocalData
import com.happy.friendogly.data.model.MessageDto
import com.happy.friendogly.data.source.ChatMessageDataSource
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.ChatMessageRepository
import com.happy.friendogly.local.mapper.toDomain
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.room.ChatMessageDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import java.time.LocalDateTime
import javax.inject.Inject

class ChatMessageRepositoryImpl
@Inject
constructor(
    private val dataSource: ChatMessageDataSource,
    private val chatMessageDao: ChatMessageDao,
) : ChatMessageRepository {
    override suspend fun getChatMessagesInRange(
        myMemberId: Long,
        chatRoomId: Long,
        offset: Int,
        limit: Int,
    ): Flow<List<ChatComponent>> =
        flow {
            val latestMessage =
                chatMessageDao.getLatestMessageByRoomId(chatRoomId)
                    ?: return@flow emit(
                        saveAndGetAllChatMessages(
                            chatRoomId = chatRoomId,
                            myMemberId = myMemberId,
                        ),
                    )

            saveNewMessages(chatRoomId, latestMessage)
            emit(
                chatMessageDao.getMessagesByRoomIdInRange(
                    chatRoomId = chatRoomId,
                    limit = limit,
                    offset = offset
                ).map { it.toDomain(myMemberId) })
        }

    override suspend fun saveMessage(
        chatRoomId: Long,
        message: ChatComponent,
    ): Flow<Unit> =
        flow {
            emit(chatMessageDao.insert(message.toData(chatRoomId)))
        }

    private suspend fun saveNewMessages(
        chatRoomId: Long,
        latestMessage: ChatMessageEntity,
    ) {
        val newMessages =
            dataSource.getChatMessagesByTime(
                chatRoomId = chatRoomId,
                since = latestMessage.createdAt,
                until = LocalDateTime.now(),
            ).last().filterNot { isSameMessage(it, latestMessage) }

        chatMessageDao.insertAll(*newMessages.toLocalData(chatRoomId).toTypedArray())
    }

    private fun isSameMessage(
        it: MessageDto,
        latestMessage: ChatMessageEntity,
    ) = it.createdAt == latestMessage.createdAt && it.content == latestMessage.content

    private suspend fun saveAndGetAllChatMessages(
        chatRoomId: Long,
        myMemberId: Long,
    ): List<ChatComponent> {
        val newMessages = dataSource.getAllChatMessages(chatRoomId).last().sortedBy { it.createdAt }
        chatMessageDao.insertAll(
            *newMessages.toLocalData(chatRoomId).toTypedArray(),
        )
        return newMessages.toDomain(myMemberId)
    }
}
