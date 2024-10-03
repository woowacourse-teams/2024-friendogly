package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.ChatMemberDto
import com.happy.friendogly.data.model.ChatRoomClubDto
import com.happy.friendogly.data.model.ChatRoomListDto
import com.happy.friendogly.data.source.ChatRoomDataSource
import com.happy.friendogly.remote.api.ChatRoomService
import com.happy.friendogly.remote.mapper.toData
import javax.inject.Inject

class ChatRoomDataSourceImpl
@Inject
constructor(private val service: ChatRoomService) : ChatRoomDataSource {
    override suspend fun getChatList(): Result<ChatRoomListDto> =
        runCatching {
            service.getChatList().data.toData()
        }

    override suspend fun getMembers(chatRoomId: Long): Result<List<ChatMemberDto>> =
        runCatching {
            service.getChatMembers(chatRoomId).data.map { it.toData() }
        }

    override suspend fun getClubs(chatRoomId: Long): Result<ChatRoomClubDto> =
        runCatching {
            service.getChatClub(chatRoomId).data.toData()
        }

    override suspend fun leaveChatRoom(chatRoomId: Long): Result<Unit> = runCatching {
        service.postLeaveChatRoom(chatRoomId)
    }
}
