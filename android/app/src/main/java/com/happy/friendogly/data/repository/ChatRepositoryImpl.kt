package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.ChatDataSource
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRooms
import com.happy.friendogly.domain.repository.ChatRepository

class ChatRepositoryImpl(private val source: ChatDataSource) : ChatRepository {
    override suspend fun getChatList(): Result<ChatRooms> =
        source.getChatList().mapCatching { it.toDomain() }

    override suspend fun getMembers(chatRoomId: Long): Result<List<ChatMember>> =
        source.getMembers(chatRoomId).mapCatching { member ->
            member.map { it.toDomain() }
        }
}
