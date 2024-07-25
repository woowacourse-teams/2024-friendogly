package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.repository.LocalRepository

class GetJwtTokenUseCase(
    private val repository: LocalRepository,
) {
    suspend operator fun invoke(): Result<JwtToken?> = repository.getJwtToken()
}
