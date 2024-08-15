package com.happy.friendogly.presentation.ui.club.common.mapper

import com.happy.friendogly.domain.model.ClubState

fun ClubState.toPresentation(): Boolean {
    return when (this) {
        ClubState.OPEN -> true
        ClubState.CLOSE -> false
    }
}
