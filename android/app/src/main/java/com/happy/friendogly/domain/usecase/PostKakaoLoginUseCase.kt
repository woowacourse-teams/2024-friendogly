package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.repository.AuthRepository

class PostKakaoLoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(accessToken: String): Result<Login> = repository.postKakaoLogin(accessToken = accessToken)
}
