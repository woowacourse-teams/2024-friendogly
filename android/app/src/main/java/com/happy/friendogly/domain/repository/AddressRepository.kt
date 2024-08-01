package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Address

interface AddressRepository {
    suspend fun getAddress(): Result<Address>

    suspend fun saveAddress(address: Address): Result<Unit>
}
