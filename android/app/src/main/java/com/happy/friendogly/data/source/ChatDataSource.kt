package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.ChatMemberDto
import com.happy.friendogly.data.model.ChatRoomListDto
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRooms

interface ChatDataSource {

    suspend fun getChatList(): Result<ChatRoomListDto>

    suspend fun getMembers(chatRoomId: Long): Result<List<ChatMemberDto>>
}
