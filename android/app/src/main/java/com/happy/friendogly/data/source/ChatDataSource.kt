package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.ChatMemberDto
import com.happy.friendogly.data.model.ChatRoomClubDto
import com.happy.friendogly.data.model.ChatRoomListDto

interface ChatDataSource {
    suspend fun getChatList(): Result<ChatRoomListDto>

    suspend fun getMembers(chatRoomId: Long): Result<List<ChatMemberDto>>

    suspend fun getClubs(chatRoomId: Long): Result<ChatRoomClubDto>
}
