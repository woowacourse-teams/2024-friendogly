package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.model.ChatRooms

interface ChatRepository {

    suspend fun getChatList():Result<ChatRooms>

    suspend fun getMembers(chatRoomId:Long):Result<List<ChatMember>>
}
