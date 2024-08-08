package com.happy.friendogly.presentation.ui.group.mapper

import com.happy.friendogly.domain.model.ClubState

fun ClubState.toPresentation(): Boolean {
    return when (this) {
        ClubState.OPEN -> true
        ClubState.CLOSE -> false
    }
}
