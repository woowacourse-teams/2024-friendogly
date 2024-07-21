package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.JwtToken
import com.woowacourse.friendogly.domain.repository.LocalRepository

class SaveJwtTokenUseCase(
    private val repository: LocalRepository,
) {
    suspend operator fun invoke(jwtToken: JwtToken) = repository.saveJwtToken(jwtToken = jwtToken)
}
