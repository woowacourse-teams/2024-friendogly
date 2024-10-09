package com.happy.friendogly.data.repository

import android.util.Log
import androidx.paging.map
import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.mapper.toLocalData
import com.happy.friendogly.data.source.ChatMessageDataSource
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.ChatMessageRepository
import com.happy.friendogly.local.mapper.toDomain
import com.happy.friendogly.local.model.ChatMessageEntity
import com.happy.friendogly.local.room.ChatRoomDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class ChatMessageRepositoryImpl
    @Inject
    constructor(
        private val dataSource: ChatMessageDataSource,
        private val chatRoomDao: ChatRoomDao,
    ) : ChatMessageRepository {
        override suspend fun getChatMessagesInRange(
            myMemberId: Long,
            chatRoomId: Long,
            offset: Int,
            limit: Int,
        ): Flow<List<ChatComponent>> =
            flow {
                val latestMessage =
                    chatRoomDao.getLatestMessageByRoomId(chatRoomId)
                        ?: return@flow emit(
                            saveAndGetAllChatMessages(
                                chatRoomId = chatRoomId,
                                myMemberId = myMemberId,
                            ),
                        )

                saveNewMessages(chatRoomId, latestMessage)

                val messages = chatRoomDao.getMessagesInRange(limit = limit, offset = offset).map { it.toDomain(myMemberId) }
                Log.d("테스트", "db : $messages")
                emit(messages)
            }

        override suspend fun saveMessage(
            chatRoomId: Long,
            message: ChatComponent,
        ): Flow<Unit> =
            flow {
                emit(chatRoomDao.addNewMessageToChatRoom(chatRoomId, message.toData(chatRoomId)))
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
                ).last().filterNot { it.createdAt == latestMessage.createdAt && it.content == latestMessage.content }

            chatRoomDao.addNewMessagesToChatRoom(chatRoomId, newMessages.toLocalData(chatRoomId))
        }

        private suspend fun saveAndGetAllChatMessages(
            chatRoomId: Long,
            myMemberId: Long,
        ): List<ChatComponent> {
            val newMessages = dataSource.getAllChatMessages(chatRoomId).last().sortedBy { it.createdAt }
            chatRoomDao.addNewMessagesToChatRoom(
                chatRoomId,
                newMessages.toLocalData(chatRoomId),
            )
            return newMessages.toDomain(myMemberId)
        }
    }
