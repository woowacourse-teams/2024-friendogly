package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.MessagingRepository
import javax.inject.Inject

class GetFCMTokenUseCase
    @Inject
    constructor(private val repository: MessagingRepository) {
        suspend operator fun invoke(): DomainResult<String, DataError.Local> = repository.getToken()
    }
