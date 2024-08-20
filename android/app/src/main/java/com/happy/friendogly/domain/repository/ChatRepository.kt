package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRoomClub
import com.happy.friendogly.domain.model.ChatRooms

interface ChatRepository {
    suspend fun getChatList(): Result<ChatRooms>

    suspend fun getMembers(chatRoomId: Long): Result<List<ChatMember>>

    suspend fun saveMessage(
        chatRoomId: Long,
        chat: ChatComponent,
    ): Result<Unit>

    suspend fun getChatMessages(
        chatRoomId: Long,
        myMemberId: Long,
    ): Result<List<ChatComponent>>

    suspend fun getChatClub(chatRoomId: Long): Result<ChatRoomClub>
}
