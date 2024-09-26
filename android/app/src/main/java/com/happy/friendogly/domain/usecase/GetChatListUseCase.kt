package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatRooms
import com.happy.friendogly.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatListUseCase
    @Inject
    constructor(
        val repository: ChatRepository,
    ) {
        suspend operator fun invoke(): Result<ChatRooms> = repository.getChatList()
    }
