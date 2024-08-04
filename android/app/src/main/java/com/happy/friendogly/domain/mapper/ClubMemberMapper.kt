package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.ClubMember
import com.happy.friendogly.domain.model.ClubPet
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel

fun ClubMember.toPresentation(): GroupDetailProfileUiModel {
    return GroupDetailProfileUiModel(
        name = name,
        imageUrl = imageUrl
    )
}

fun ClubPet.toPresentation(): GroupDetailProfileUiModel {
    return GroupDetailProfileUiModel(
        name = name,
        imageUrl = imageUrl
    )
}
