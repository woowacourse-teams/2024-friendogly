package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.AuthRepository

class PostLogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): DomainResult<Unit, DataError.Network> = repository.postLogout()
}
