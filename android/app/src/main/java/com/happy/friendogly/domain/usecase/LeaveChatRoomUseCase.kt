package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.ChatRoomRepository
import javax.inject.Inject

class LeaveChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    suspend operator fun invoke(
        chatRoomId:Long
    ):Result<Unit> = repository.leaveChatRoom(chatRoomId)
}