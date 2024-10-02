package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.ChatRoomRepository
import javax.inject.Inject

class GetChatMessagesUseCase
    @Inject
    constructor(
        private val repository: ChatRoomRepository,
    ) {
        suspend operator fun invoke(
            chatRoomId: Long,
            myMemberId: Long,
        ): Result<List<ChatComponent>> = repository.getChatMessages(chatRoomId, myMemberId)
    }
