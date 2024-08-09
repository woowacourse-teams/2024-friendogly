package com.happy.friendogly.presentation.ui.club.mapper

import com.happy.friendogly.domain.model.ClubMember
import com.happy.friendogly.domain.model.ClubPet
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel

fun ClubMember.toPresentation(): ClubDetailProfileUiModel {
    return ClubDetailProfileUiModel(
        name = name,
        imageUrl = imageUrl,
    )
}

fun ClubPet.toPresentation(): ClubDetailProfileUiModel {
    return ClubDetailProfileUiModel(
        name = name,
        imageUrl = imageUrl.ifEmpty { null },
    )
}
