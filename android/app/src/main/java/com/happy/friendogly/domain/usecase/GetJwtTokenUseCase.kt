package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.JwtToken
import com.happy.friendogly.domain.repository.TokenRepository
import javax.inject.Inject

class GetJwtTokenUseCase @Inject constructor(
    private val repository: TokenRepository,
) {
    suspend operator fun invoke(): DomainResult<JwtToken?, DataError.Local> = repository.getJwtToken()
}
