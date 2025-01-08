package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase
    @Inject
    constructor(
        private val repository: AddressRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = repository.deleteAddress()
    }
