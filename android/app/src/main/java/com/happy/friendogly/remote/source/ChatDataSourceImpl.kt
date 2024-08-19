package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.ChatMemberDto
import com.happy.friendogly.data.model.ChatRoomClubDto
import com.happy.friendogly.data.model.ChatRoomListDto
import com.happy.friendogly.data.source.ChatDataSource
import com.happy.friendogly.remote.api.ChatService
import com.happy.friendogly.remote.mapper.toData

class ChatDataSourceImpl(private val service: ChatService) : ChatDataSource {
    override suspend fun getChatList(): Result<ChatRoomListDto> =
        runCatching {
            service.getChatList().data.toData()
        }

    override suspend fun getMembers(chatRoomId: Long): Result<List<ChatMemberDto>> =
        runCatching {
            service.getChatMembers(chatRoomId).body()?.map { it.toData() } ?: emptyList()
        }

    override suspend fun getClubs(chatRoomId: Long): Result<ChatRoomClubDto> = runCatching {
        service.getChatClub(chatRoomId).data.toData()
    }
}
