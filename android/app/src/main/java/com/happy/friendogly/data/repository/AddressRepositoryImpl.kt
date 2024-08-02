package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.model.AddressDto
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.domain.model.Address
import com.happy.friendogly.domain.repository.AddressRepository

class AddressRepositoryImpl(
    private val addressDataSource: AddressDataSource,
) : AddressRepository {
    override suspend fun getAddress(): Result<Address> =
        addressDataSource.getAddress().mapCatching {
            if (isInValidAddress(it)) throw Exception()
            it.toDomain()
        }

    override suspend fun saveAddress(address: Address): Result<Unit> =
        addressDataSource.saveAddress(addressDto = address.toData())

    override suspend fun deleteAddress(): Result<Unit> =
        addressDataSource.deleteAddress()

    private fun isInValidAddress(addressDto: AddressDto) =
        addressDto.thoroughfare.isEmpty() || addressDto.adminArea.isEmpty() || addressDto.subLocality.isEmpty()
}
