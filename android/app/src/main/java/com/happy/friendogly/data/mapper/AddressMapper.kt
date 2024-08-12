package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.UserAddressDto
import com.happy.friendogly.domain.model.UserAddress

fun UserAddressDto.toDomain(): UserAddress {
    return UserAddress(
        thoroughfare = thoroughfare ?: "",
        subLocality = subLocality ?: "",
        adminArea = adminArea,
    )
}

fun UserAddress.toData(): UserAddressDto {
    return UserAddressDto(
        thoroughfare = thoroughfare,
        subLocality = subLocality,
        adminArea = adminArea,
    )
}
