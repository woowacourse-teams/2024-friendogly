package com.happy.friendogly.local.source

import com.happy.friendogly.data.model.AddressDto
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.local.di.AddressModule
import kotlinx.coroutines.flow.first

class AddressDataSourceImpl(
    private val addressModule: AddressModule,
) : AddressDataSource {
    override suspend fun getAddress(): Result<AddressDto> = runCatching {
        AddressDto(
            thoroughfare = addressModule.thoroughfare.first(),
            subLocality = addressModule.subLocality.first(),
            adminArea = addressModule.adminArea.first()
        )
    }

    override suspend fun saveAddress(addressDto: AddressDto): Result<Unit> =
        runCatching {
            addressModule.saveAddress(addressDto)
        }

    override suspend fun deleteAddress(): Result<Unit>  = runCatching {
        addressModule.deleteAddressData()
    }
}
