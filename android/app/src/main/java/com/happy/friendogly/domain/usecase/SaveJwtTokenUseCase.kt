package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.repository.TokenRepository

class SaveJwtTokenUseCase(
    private val repository: TokenRepository,
) {
    suspend operator fun invoke(jwtToken: JwtToken) = repository.saveJwtToken(jwtToken = jwtToken)
}
