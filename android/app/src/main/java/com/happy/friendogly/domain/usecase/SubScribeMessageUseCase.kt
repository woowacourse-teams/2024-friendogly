package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubScribeMessageUseCase(private val repository: WebSocketRepository) {
    suspend operator fun invoke(chatRoomId: Long, myMemberId: Long): Flow<ChatComponent> =
        repository.subscribeMessage(chatRoomId, myMemberId)
}
