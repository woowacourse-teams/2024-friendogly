package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WebSocketRepository
import javax.inject.Inject

class DisconnectWebsocketUseCase @Inject constructor(
    private val repository: WebSocketRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.disconnect()
}
