package com.happy.friendogly.presentation.ui.club.common.mapper

import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailUiModel
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailViewType

fun ClubDetail.toPresentation(): ClubDetailUiModel {
    return ClubDetailUiModel(
        clubId = id,
        filters = allowedGender.toGenderGroupFilters() + allowedSize.toSizeGroupFilters(),
        content = content,
        title = title,
        petProfiles =
            petDetails
                .map { it.toPresentation() }
                .sortedByDescending { it.isMyPet },
        userProfiles = memberDetails.map { it.toPresentation() },
        clubDate = createdAt,
        currentNumberOfPeople = currentMemberCount,
        clubLeaderName = ownerMemberName,
        clubState = status,
        clubLeaderImage = ownerImageUrl?.ifEmpty { null },
        clubLocation = address.toPresentation(),
        clubPoster = imageUrl?.ifEmpty { null },
        maximumNumberOfPeople = memberCapacity,
        isUserPetEmpty = isMyPetsEmpty,
        clubDetailViewType =
            ClubDetailViewType.from(
                isMine = isMine,
                isOpenState = status == ClubState.OPEN,
                isMyParticipated = alreadyParticipate,
                canParticipation = canParticipate,
                isUserPetEmpty = isMyPetsEmpty,
            ),
        chatRoomId = chatRoomId,
    )
}
