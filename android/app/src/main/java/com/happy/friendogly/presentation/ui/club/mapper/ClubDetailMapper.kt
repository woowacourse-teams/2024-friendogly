package com.happy.friendogly.presentation.ui.club.mapper

import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailUiModel
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType

fun ClubDetail.toPresentation(): ClubDetailUiModel {
    return ClubDetailUiModel(
        clubId = id,
        filters = allowedGender.toGenderGroupFilters() + allowedSize.toSizeGroupFilters(),
        content = content,
        title = title,
        petProfiles = petDetails.map { it.toPresentation() },
        userProfiles = memberDetails.map { it.toPresentation() },
        clubDate = createdAt,
        currentNumberOfPeople = currentMemberCount,
        clubLeaderName = ownerMemberName,
        clubLeaderImage = ownerImageUrl.ifEmpty { null },
        clubLocation = address.toPresentation(),
        clubPoster = imageUrl.ifEmpty { null },
        maximumNumberOfPeople = memberCapacity,
        clubDetailViewType =
            ClubDetailViewType.from(
                isMine = isMine,
                isMyParticipated = alreadyParticipate,
                canParticipation = canParticipate,
            ),
    )
}
