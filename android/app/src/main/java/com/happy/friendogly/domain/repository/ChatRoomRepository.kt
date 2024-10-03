package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRoomClub
import com.happy.friendogly.domain.model.ChatRooms

interface ChatRoomRepository {
    suspend fun getChatList(): Result<ChatRooms>

    suspend fun getMembers(chatRoomId: Long): Result<List<ChatMember>>

    suspend fun getChatClub(chatRoomId: Long): Result<ChatRoomClub>

    suspend fun leaveChatRoom(chatRoomId: Long): Result<Unit>
}
