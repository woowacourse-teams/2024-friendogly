package com.happy.friendogly.local.source

import com.happy.friendogly.data.model.UserAddressDto
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.local.di.AddressModule
import kotlinx.coroutines.flow.first

class AddressDataSourceImpl(
    private val addressModule: AddressModule,
) : AddressDataSource {
    override suspend fun getAddress(): Result<UserAddressDto> =
        runCatching {
            UserAddressDto(
                thoroughfare = addressModule.thoroughfare.first(),
                subLocality = addressModule.subLocality.first(),
                adminArea = addressModule.adminArea.first(),
            )
        }

    override suspend fun saveAddress(userAddressDto: UserAddressDto): Result<Unit> =
        runCatching {
            addressModule.saveAddress(userAddressDto)
        }

    override suspend fun deleteAddress(): Result<Unit> =
        runCatching {
            addressModule.deleteAddressData()
        }
}
