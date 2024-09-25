package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.repository.AddressRepository
import javax.inject.Inject

class SaveAddressUseCase @Inject constructor(
    private val repository: AddressRepository,
) {
    suspend operator fun invoke(userAddress: UserAddress) = repository.saveAddress(userAddress = userAddress)
}
