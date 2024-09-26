package com.happy.friendogly.presentation.ui.playground.mapper

import com.happy.friendogly.presentation.ui.playground.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.playground.uimodel.PetDetailInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.WalkStatusInfoUiModel
import com.naver.maps.map.overlay.Marker

fun PlaygroundInfo.toPresentation(marker: Marker): FootprintInfoUiModel {
    return FootprintInfoUiModel(
        marker = marker,
        walkStatusInfo = toWalkStatusPresentation(),
        petsDetailInfo = toPetDetailInfoPresentation(),
    )
}

fun PlaygroundInfo.toWalkStatusPresentation(): WalkStatusInfoUiModel {
    return WalkStatusInfoUiModel(
        walkStatus = walkStatus,
        changedWalkStatusTime = changedWalkStatusTime,
    )
}

fun PlaygroundInfo.toPetDetailInfoPresentation(): List<PetDetailInfoUiModel> {
    return petDetails.map { petDetail ->
        PetDetailInfoUiModel(
            memberId = memberId,
            memberName = memberName,
            petDetail = petDetail,
        )
    }
}
