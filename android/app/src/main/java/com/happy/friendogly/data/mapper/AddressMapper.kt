package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.AddressDto
import com.happy.friendogly.domain.model.Address

fun AddressDto.toDomain(): Address {
    return Address(
        address = address,
        subLocality = subLocality,
        adminArea = adminArea,
    )
}

fun Address.toData(): AddressDto {
    return AddressDto(
        address = address,
        subLocality = subLocality,
        adminArea = adminArea,
    )
}
