package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Login
import com.happy.friendogly.domain.repository.AuthRepository
import javax.inject.Inject

class PostKakaoLoginUseCase
    @Inject
    constructor(private val repository: AuthRepository) {
        suspend operator fun invoke(accessToken: String): DomainResult<Login, DataError.Network> =
            repository.postKakaoLogin(accessToken = accessToken)
    }
