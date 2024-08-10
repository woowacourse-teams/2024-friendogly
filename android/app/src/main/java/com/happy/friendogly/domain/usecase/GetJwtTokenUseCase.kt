package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.repository.TokenRepository

class GetJwtTokenUseCase(
    private val repository: TokenRepository,
) {
    suspend operator fun invoke(): Result<JwtToken?> = repository.getJwtToken()
}
