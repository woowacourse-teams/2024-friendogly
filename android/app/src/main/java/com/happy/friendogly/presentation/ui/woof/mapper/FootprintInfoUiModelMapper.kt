package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoPetDetailUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoWalkStatusUiModel

fun FootprintInfo.toWalkStatusUiModel(): FootprintInfoWalkStatusUiModel {
    return FootprintInfoWalkStatusUiModel(
        walkStatus = walkStatus,
        changedWalkStatusTime = changedWalkStatusTime,
    )
}

fun FootprintInfo.toPetDetailsUiModel(): List<FootprintInfoPetDetailUiModel> {
    return petDetails.map { petDetail ->
        FootprintInfoPetDetailUiModel(
            memberId = memberId,
            memberName = memberName,
            petDetail = petDetail,
        )
    }
}
