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
        addressDataSource.getAddress().mapCatching {
            val address = it.toDomain()
            if (isInValidAddress(address)) throw Exception()
            address
        }

    override suspend fun saveAddress(userAddress: UserAddress): Result<Unit> =
        addressDataSource.saveAddress(userAddressDto = userAddress.toData()).mapCatching {
            if (isInValidAddress(userAddress)) throw Exception()
        }

    override suspend fun deleteAddress(): Result<Unit> =
        addressDataSource.deleteAddress()

    private fun isInValidAddress(userAddress: UserAddress) =
        userAddress.thoroughfare.isEmpty() || userAddress.adminArea.isEmpty() || userAddress.subLocality.isEmpty()
}
