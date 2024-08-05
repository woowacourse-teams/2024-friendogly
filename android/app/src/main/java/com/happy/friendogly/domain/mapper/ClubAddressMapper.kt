package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.ClubAddress

fun ClubAddress.toPresentation(): String {
    return "$province $city $village"
}
