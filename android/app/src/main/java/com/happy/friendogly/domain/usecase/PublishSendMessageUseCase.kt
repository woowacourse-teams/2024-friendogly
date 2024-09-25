package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WebSocketRepository
import javax.inject.Inject

class PublishSendMessageUseCase
    @Inject
    constructor(
        private val repository: WebSocketRepository,
    ) {
        suspend operator fun invoke(
            chatRoomId: Long,
            content: String,
        ): Result<Unit> = repository.publishSend(chatRoomId, content)
    }
