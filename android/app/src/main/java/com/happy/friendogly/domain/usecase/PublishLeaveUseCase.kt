package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WebSocketRepository
import javax.inject.Inject

class PublishLeaveUseCase
    @Inject
    constructor(
        private val repository: WebSocketRepository,
    ) {
        suspend operator fun invoke(chatRoomId: Long): Result<Unit> = repository.publishLeave(chatRoomId)
    }
