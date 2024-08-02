package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.UserAddressDto

interface AddressDataSource {
    suspend fun getAddress(): Result<UserAddressDto>

    suspend fun saveAddress(userAddressDto: UserAddressDto): Result<Unit>

    suspend fun deleteAddress() : Result<Unit>
}
