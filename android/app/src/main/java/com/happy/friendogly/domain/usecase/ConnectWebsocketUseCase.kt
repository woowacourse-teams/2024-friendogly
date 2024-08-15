package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WebSocketRepository

class ConnectWebsocketUseCase(
    private val repository: WebSocketRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.connect()
}
