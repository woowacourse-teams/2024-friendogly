package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.ChatMessageRepository
import javax.inject.Inject

class GetChatMessagesUseCase
    @Inject
    constructor(
        private val repository: ChatMessageRepository,
    ) {
        suspend operator fun invoke(
            myMemberId: Long,
            chatRoomId: Long,
        ): Result<List<ChatComponent>> = repository.getAllChatMessages(myMemberId, chatRoomId)
    }
