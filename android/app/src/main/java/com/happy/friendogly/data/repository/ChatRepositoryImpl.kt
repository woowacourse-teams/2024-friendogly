package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRooms
import com.happy.friendogly.domain.repository.ChatRepository
import com.happy.friendogly.remote.api.ChatService
import com.happy.friendogly.remote.mapper.toData

class ChatRepositoryImpl(private val service: ChatService):ChatRepository {
    override suspend fun getChatList(): Result<ChatRooms> =
        runCatching {
            service.getChatList().data.toData().toDomain()
        }


    override suspend fun getMembers(chatRoomId:Long): Result<List<ChatMember>> = runCatching {
        service.getChatMembers(chatRoomId).data.map { it.toData() }.map { it.toDomain() }
    }
}
