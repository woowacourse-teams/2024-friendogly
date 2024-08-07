package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.AddressRepository

class DeleteAddressUseCase(
    private val repository: AddressRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.deleteAddress()
}
