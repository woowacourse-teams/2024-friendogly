package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.UserAddress

fun ClubAddress.toPresentation(): String {
    return "$province $city $village"
}

fun UserAddress.toSizeType(): ClubAddress {
    return ClubAddress(
        province = adminArea ,
        city = subLocality ,
        village = thoroughfare,
    )
}
