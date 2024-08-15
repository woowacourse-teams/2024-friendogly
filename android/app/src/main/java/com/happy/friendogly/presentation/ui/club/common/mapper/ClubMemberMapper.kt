package com.happy.friendogly.presentation.ui.club.common.mapper

import com.happy.friendogly.domain.model.ClubMember
import com.happy.friendogly.domain.model.ClubPet
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel

fun ClubMember.toPresentation(): ClubDetailProfileUiModel {
    return ClubDetailProfileUiModel(
        id = id,
        name = name,
        imageUrl = imageUrl?.ifEmpty { null },
        isPet = false,
        isMyPet = false,
    )
}

fun ClubPet.toPresentation(): ClubDetailProfileUiModel {
    return ClubDetailProfileUiModel(
        id = id,
        name = name,
        imageUrl = imageUrl?.ifEmpty { null },
        isPet = true,
        isMyPet = isMine,
    )
}
