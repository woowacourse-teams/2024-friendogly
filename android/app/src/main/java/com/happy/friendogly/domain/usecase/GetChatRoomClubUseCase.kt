package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatRoomClub
import com.happy.friendogly.domain.repository.ChatRoomRepository
import javax.inject.Inject

class GetChatRoomClubUseCase
    @Inject
    constructor(
        private val repository: ChatRoomRepository,
    ) {
        suspend operator fun invoke(chatRoomId: Long): Result<ChatRoomClub> = repository.getChatClub(chatRoomId)
    }
