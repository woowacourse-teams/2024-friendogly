package com.happy.friendogly.local.source

import com.happy.friendogly.data.model.AddressDto
import com.happy.friendogly.data.source.AddressDataSource
import com.happy.friendogly.local.di.AddressModule
import kotlinx.coroutines.flow.first

class AddressDataSourceImpl(
    private val addressModule: AddressModule,
) : AddressDataSource {
    override suspend fun getAddress(): Result<AddressDto> {
        return runCatching {
            val location = addressModule.myAddress.first()
            AddressDto(location = location)
        }
    }

    override suspend fun saveAddress(addressDto: AddressDto): Result<Unit> =
        runCatching {
            val location = addressDto.location
            addressModule.saveLocation(location)
        }
}
