package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.WalkStatusInfoUiModel
import com.naver.maps.map.overlay.Marker

fun FootprintInfo.toPresentation(marker: Marker): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        marker = marker,
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
