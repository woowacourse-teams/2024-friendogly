package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatRoomClub
import com.happy.friendogly.domain.repository.ChatRepository

class GetChatRoomClubUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatRoomId: Long): Result<ChatRoomClub> =
        repository.getChatClub(chatRoomId)
}
