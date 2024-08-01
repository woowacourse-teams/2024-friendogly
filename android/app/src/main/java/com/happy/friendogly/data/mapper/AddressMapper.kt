package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.AddressDto
import com.happy.friendogly.domain.model.Address

fun AddressDto.toDomain(): Address {
    return Address(
        location = location
    )
}

fun Address.toData(): AddressDto {
    return AddressDto(
        location = location
    )
}
