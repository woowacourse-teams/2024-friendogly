package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.FootprintInfo
import com.happy.friendogly.presentation.model.FootprintInfoUiModel

fun FootprintInfo.toPresentation(): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        memberName = memberName,
        petName = petName,
        petDescription = petDescription,
        petBirthDate = petBirthDate,
        petSizeType = petSizeType,
        petGender = petGender,
        footprintImageUrl = footprintImageUrl,
        walkStatus = walkStatus,
        startWalkTime = startWalkTime,
        endWalkTime = endWalkTime,
        createdAt = createdAt,
        isMine = isMine,
    )
}
