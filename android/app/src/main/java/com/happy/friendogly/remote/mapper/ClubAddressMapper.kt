package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubAddressDto
import com.happy.friendogly.remote.model.response.ClubAddressResponse

fun ClubAddressResponse.toData(): ClubAddressDto {
    return ClubAddressDto(
        province = province,
        city = city,
        village = village,
    )
}
