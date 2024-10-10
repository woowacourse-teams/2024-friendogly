package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.repository.ChatRoomRepository
import javax.inject.Inject

class GetChatMemberUseCase
    @Inject
    constructor(
        val repository: ChatRoomRepository,
    ) {
        suspend operator fun invoke(chatRoomId: Long): Result<List<ChatMember>> = repository.getMembers(chatRoomId)
    }
