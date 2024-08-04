package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubAddressDto
import com.happy.friendogly.remote.model.request.ClubAddressRequest
import com.happy.friendogly.remote.model.response.ClubAddressResponse

fun ClubAddressResponse.toData() : ClubAddressDto {
    return ClubAddressDto(
        province = province,
        city = city,
        village = village,
    )
}

fun ClubAddressDto.toRemote(): ClubAddressRequest {
    return ClubAddressRequest(
        province = province,
        city = city,
        village = village,
    )
}
