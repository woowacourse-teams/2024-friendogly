package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.FootprintMarkBtnInfo
import com.happy.friendogly.presentation.model.FootprintMarkBtnInfoUiModel

fun FootprintMarkBtnInfo.toPresentation(): FootprintMarkBtnInfoUiModel {
    return FootprintMarkBtnInfoUiModel(
        isClickable = isMarkBtnClickable(),
        remainingTime = remainingTime().toString(),
    )
}
