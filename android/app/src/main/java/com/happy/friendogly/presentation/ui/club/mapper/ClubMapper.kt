package com.happy.friendogly.presentation.ui.club.mapper

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.presentation.ui.club.list.ClubListUiModel
import com.happy.friendogly.presentation.ui.club.model.ClubPet

fun Club.toPresentation(): ClubListUiModel {
    return ClubListUiModel(
        clubId = id,
        clubPoster = imageUrl?.ifEmpty { null },
        maximumNumberOfPeople = memberCapacity,
        clubLocation = address.toPresentation(),
        clubLeaderName = ownerMemberName,
        title = title,
        filters = allowedGender.toGenderGroupFilters() + allowedSize.toSizeGroupFilters(),
        clubDate = createdAt,
        currentNumberOfPeople = currentMemberCount,
        content = content,
        clubPets = petImageUrls.map { ClubPet(it?.ifEmpty { null }) },
        canParticipate = status.toPresentation(),
    )
}
