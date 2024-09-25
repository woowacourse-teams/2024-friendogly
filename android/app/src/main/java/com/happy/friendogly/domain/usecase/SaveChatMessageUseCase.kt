package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatComponent
import com.happy.friendogly.domain.repository.ChatRepository
import javax.inject.Inject

class SaveChatMessageUseCase @Inject constructor(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(
        chatRoomId: Long,
        chatComponent: ChatComponent,
    ): Result<Unit> = repository.saveMessage(chatRoomId, chatComponent)
}
