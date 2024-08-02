package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.domain.model.Address
import com.happy.friendogly.domain.repository.AddressRepository

class AddressRepositoryImpl(
    private val addressDataSource: AddressDataSource,
) : AddressRepository {
    override suspend fun getAddress(): Result<Address> =
        addressDataSource.getAddress().mapCatching {
            val address = it.toDomain()
            if (isInValidAddress(address)) throw Exception()
            address
        }

    override suspend fun saveAddress(address: Address): Result<Unit> =
        addressDataSource.saveAddress(addressDto = address.toData()).mapCatching {
            if (isInValidAddress(address)) throw Exception()
        }

    override suspend fun deleteAddress(): Result<Unit> =
        addressDataSource.deleteAddress()

    private fun isInValidAddress(address: Address) =
        address.thoroughfare.isEmpty() || address.adminArea.isEmpty() || address.subLocality.isEmpty()
}
