package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoPetDetailUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoWalkStatusUiModel

fun FootprintInfo.toWalkStatusPresentation(): FootprintInfoWalkStatusUiModel {
    return FootprintInfoWalkStatusUiModel(
        walkStatus = walkStatus,
        changedWalkStatusTime = changedWalkStatusTime,
    )
}

fun FootprintInfo.toPetDetailsPresentation(): List<FootprintInfoPetDetailUiModel> {
    return petDetails.map { petDetail ->
        FootprintInfoPetDetailUiModel(
            memberId = memberId,
            memberName = memberName,
            petDetail = petDetail,
        )
    }
}
