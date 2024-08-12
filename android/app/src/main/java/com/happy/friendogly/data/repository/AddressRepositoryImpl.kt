package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.repository.AddressRepository

class AddressRepositoryImpl(
    private val addressDataSource: AddressDataSource,
) : AddressRepository {
    override suspend fun getAddress(): Result<UserAddress> =
        addressDataSource.getAddress().mapCatching { address ->
            address.toDomain()
        }

    override suspend fun saveAddress(userAddress: UserAddress): Result<Unit> =
        addressDataSource.saveAddress(userAddressDto = userAddress.toData())

    override suspend fun deleteAddress(): Result<Unit> = addressDataSource.deleteAddress()
}
