package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.ChatMember
import com.happy.friendogly.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatMemberUseCase @Inject constructor(
    val repository: ChatRepository,
) {
    suspend operator fun invoke(chatRoomId: Long): Result<List<ChatMember>> = repository.getMembers(chatRoomId)
}
