package com.happy.friendogly.presentation.ui.club.common.mapper

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.common.model.ClubPet

fun List<Club>.toPresentation(): List<ClubItemUiModel> {
    return this.map { it.toPresentation() }
}

fun Club.toPresentation(): ClubItemUiModel {
    return ClubItemUiModel(
        clubId = id,
        clubPoster = imageUrl?.ifEmpty { null },
        maximumNumberOfPeople = memberCapacity,
        clubLocation = address.toPresentation(),
        clubLeaderName = ownerMemberName,
        title = title,
        clubDate = createdAt,
        currentNumberOfPeople = currentMemberCount,
        content = content,
        clubPets = petImageUrls.map { ClubPet(it?.ifEmpty { null }) },
        clubState = status,
    )
}
