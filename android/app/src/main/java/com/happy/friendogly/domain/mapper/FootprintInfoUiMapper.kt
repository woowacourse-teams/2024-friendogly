package com.happy.friendogly.domain.mapper

import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel

fun FootprintInfo.toPresentation(): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        memberName = memberName,
        walkStatus = walkStatus,
        changedWalkStatusTime = changedWalkStatusTime,
        pets = pets.toPresentation(),
        isMine = isMine,
    )
}
