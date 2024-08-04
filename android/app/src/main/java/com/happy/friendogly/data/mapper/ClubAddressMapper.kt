package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubAddressDto
import com.happy.friendogly.domain.model.ClubAddress

fun ClubAddressDto.toDomain(): ClubAddress {
    return ClubAddress(
        province = province,
        city = city,
        village = village,
    )
}

fun ClubAddress.toData(): ClubAddressDto {
    return ClubAddressDto(
        province = province,
        city = city,
        village = village,
    )
}
