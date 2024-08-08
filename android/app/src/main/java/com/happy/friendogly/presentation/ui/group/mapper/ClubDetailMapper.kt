package com.happy.friendogly.presentation.ui.group.mapper

import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.presentation.ui.group.detail.GroupDetailUiModel
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType

fun ClubDetail.toPresentation(): GroupDetailUiModel {
    return GroupDetailUiModel(
        groupId = id,
        filters = allowedGender.toGenderGroupFilters() + allowedSize.toSizeGroupFilters(),
        content = content,
        title = title,
        dogProfiles = petDetails.map { it.toPresentation() },
        userProfiles = memberDetails.map { it.toPresentation() },
        groupDate = createdAt,
        currentNumberOfPeople = currentMemberCount,
        groupLeader = ownerMemberName,
        groupLeaderImage = ownerImageUrl.ifEmpty { null },
        groupLocation = address.toPresentation(),
        groupPoster = imageUrl.ifEmpty { null },
        maximumNumberOfPeople = memberCapacity,
        groupDetailViewType =
            GroupDetailViewType.from(
                isMine = isMine,
                isMyParticipated = alreadyParticipate,
                isParticipable = canParticipate,
            ),
    )
}
