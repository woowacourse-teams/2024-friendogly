package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WebSocketRepository

class PublishSendMessageUseCase(
    private val repository: WebSocketRepository
) {
    suspend operator fun invoke(chatRoomId: Long, content: String): Result<Unit> =
        repository.publishSend(chatRoomId, content)
}
