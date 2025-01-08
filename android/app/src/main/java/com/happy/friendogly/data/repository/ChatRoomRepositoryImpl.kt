package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.ChatRoomDataSource
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRoomClub
import com.happy.friendogly.domain.model.ChatRooms
import com.happy.friendogly.domain.repository.ChatRoomRepository
import com.happy.friendogly.local.mapper.toDomain
import javax.inject.Inject

class ChatRoomRepositoryImpl
    @Inject
    constructor(
        private val source: ChatRoomDataSource,
    ) : ChatRoomRepository {
        override suspend fun getChatList(): Result<ChatRooms> = source.getChatList().mapCatching { it.toDomain() }

        override suspend fun getMembers(chatRoomId: Long): Result<List<ChatMember>> =
            source.getMembers(chatRoomId).mapCatching { member ->
                member.map { it.toDomain() }
            }

        override suspend fun getChatClub(chatRoomId: Long): Result<ChatRoomClub> = source.getClubs(chatRoomId).mapCatching { it.toDomain() }

        override suspend fun leaveChatRoom(chatRoomId: Long): Result<Unit> = source.leaveChatRoom(chatRoomId)
    }
