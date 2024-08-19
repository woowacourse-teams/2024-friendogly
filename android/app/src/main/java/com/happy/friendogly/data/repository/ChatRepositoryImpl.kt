package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.ChatDataSource
import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRoomClub
import com.happy.friendogly.domain.model.ChatRooms
import com.happy.friendogly.domain.model.Message
import com.happy.friendogly.domain.repository.ChatRepository
import com.happy.friendogly.local.mapper.toData
import com.happy.friendogly.local.mapper.toDomain
import com.happy.friendogly.local.room.ChatRoomDao

class ChatRepositoryImpl(
    private val source: ChatDataSource,
    private val chatRoomDao: ChatRoomDao,
) : ChatRepository {
    override suspend fun getChatList(): Result<ChatRooms> = source.getChatList().mapCatching { it.toDomain() }

    override suspend fun getMembers(chatRoomId: Long): Result<List<ChatMember>> =
        source.getMembers(chatRoomId).mapCatching { member ->
            member.map { it.toDomain() }
        }

    override suspend fun saveMessage(
        chatRoomId: Long,
        chat: ChatComponent,
    ): Result<Unit> =
        runCatching {
            val message =
                when (chat) {
                    is ChatComponent.Date -> chat.toData()
                    is ChatComponent.Enter -> chat.toData()
                    is ChatComponent.Leave -> chat.toData()
                    is Message.Mine -> chat.toData()
                    is Message.Other -> chat.toData()
                }
            chatRoomDao.addMessageToChatRoom(chatRoomId, message)
        }

    override suspend fun getChatMessages(
        chatRoomId: Long,
        myMemberId: Long,
    ): Result<List<ChatComponent>> =
        runCatching {
            chatRoomDao.getMessagesByRoomId(chatRoomId).map { it.toDomain(myMemberId) }
        }

    override suspend fun getChatClub(chatRoomId: Long): Result<ChatRoomClub> = source.getClubs(chatRoomId).mapCatching { it.toDomain() }
}
