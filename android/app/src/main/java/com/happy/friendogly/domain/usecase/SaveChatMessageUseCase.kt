package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.ChatMessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveChatMessageUseCase
    @Inject
    constructor(
        private val repository: ChatMessageRepository,
    ) {
        suspend operator fun invoke(
            chatRoomId: Long,
            message: ChatComponent,
        ): Flow<Unit> = repository.saveMessage(chatRoomId, message)
    }
