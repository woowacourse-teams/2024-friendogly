package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.presentation.ui.group.list.GroupListUiModel
import com.happy.friendogly.presentation.ui.group.list.model.GroupWoof

fun Club.toPresentation(): GroupListUiModel {
    return GroupListUiModel(
        groupId = id,
        groupPoster = imageUrl.ifEmpty { null },
        maximumNumberOfPeople = memberCapacity,
        groupLocation = address.toPresentation(),
        groupLeader = ownerMemberName,
        title = title,
        filters = allowedGender.toGenderGroupFilters() + allowedSize.toSizeGroupFilters(),
        groupDate = createdAt,
        currentNumberOfPeople = currentMemberCount,
        content = content,
        groupWoofs = petImageUrls.map { GroupWoof(it.ifEmpty { null }) },
        isParticipable = status.toPresentation(),
    )
}
