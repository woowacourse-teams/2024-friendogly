package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.UserAddress

interface AddressRepository {
    suspend fun getAddress(): Result<UserAddress>

    suspend fun saveAddress(userAddress: UserAddress): Result<Unit>

    suspend fun deleteAddress(): Result<Unit>
}
