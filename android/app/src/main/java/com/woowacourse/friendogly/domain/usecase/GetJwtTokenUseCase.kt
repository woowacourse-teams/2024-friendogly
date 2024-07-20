package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.JwtToken
import com.woowacourse.friendogly.domain.repository.LocalRepository

class GetJwtTokenUseCase(
    private val repository: LocalRepository,
) {
    suspend operator fun invoke(): Result<JwtToken?> = repository.getJwtToken()
}
