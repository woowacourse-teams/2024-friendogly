package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.WalkStatusInfoUiModel

fun FootprintInfo.toPresentation(): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        walkStatusInfo = toWalkStatusPresentation(),
        petsDetailInfo = toPetDetailsPresentation(),
    )
}

fun FootprintInfo.toWalkStatusPresentation(): WalkStatusInfoUiModel {
    return WalkStatusInfoUiModel(
        walkStatus = walkStatus,
        changedWalkStatusTime = changedWalkStatusTime,
    )
}

fun FootprintInfo.toPetDetailsPresentation(): List<PetDetailInfoUiModel> {
    return petDetails.map { petDetail ->
        PetDetailInfoUiModel(
            memberId = memberId,
            memberName = memberName,
            petDetail = petDetail,
        )
    }
}
