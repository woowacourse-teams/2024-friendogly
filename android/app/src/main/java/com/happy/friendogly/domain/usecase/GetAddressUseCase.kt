package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val repository: AddressRepository,
) {
    suspend operator fun invoke(): Result<UserAddress> = repository.getAddress()
}
