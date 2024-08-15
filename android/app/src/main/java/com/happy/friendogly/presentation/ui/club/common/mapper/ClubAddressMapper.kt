package com.happy.friendogly.presentation.ui.club.common.mapper

import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.UserAddress

fun ClubAddress.toPresentation(): String {
    val city = if (this.city.isNullOrEmpty()) "" else this.city
    val village = if (this.village.isNullOrEmpty()) "" else this.village
    return "$province $city $village"
}

fun UserAddress.toDomain(): ClubAddress {
    return ClubAddress(
        province = adminArea,
        city = subLocality,
        village = thoroughfare,
    )
}
