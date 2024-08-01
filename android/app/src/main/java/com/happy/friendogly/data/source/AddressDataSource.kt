package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.AddressDto
import com.happy.friendogly.domain.model.Address

interface AddressDataSource {
    suspend fun getAddress(): Result<AddressDto>

    suspend fun saveAddress(addressDto: AddressDto): Result<Unit>
}
