package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Address
import com.happy.friendogly.domain.repository.AddressRepository

class SaveAddressUseCase(
    private val repository: AddressRepository,
) {
    suspend operator fun invoke(address:Address) = repository.saveAddress(address=address)
}
