package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.adapter.PetDetailAdapter.Companion.FIRST_PET_DETAIL_VIEW_TYPE
import com.happy.friendogly.presentation.ui.woof.adapter.PetDetailAdapter.Companion.PET_DETAIL_VIEW_TYPE
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailInfoUiModel
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
    return petDetails.mapIndexed { index, petDetail ->
        PetDetailInfoUiModel(
            memberId = memberId,
            memberName = memberName,
            petDetail = petDetail,
            viewType = if (index == 0) FIRST_PET_DETAIL_VIEW_TYPE else PET_DETAIL_VIEW_TYPE,
        )
    }
}
