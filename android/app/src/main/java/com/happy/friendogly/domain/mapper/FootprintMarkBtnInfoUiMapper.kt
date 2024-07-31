package com.happy.friendogly.domain.mapper

import com.happy.friendogly.presentation.ui.woof.model.FootprintMarkBtnInfo
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintMarkBtnInfoUiModel

fun FootprintMarkBtnInfo.toPresentation(): FootprintMarkBtnInfoUiModel {
    return FootprintMarkBtnInfoUiModel(
        isClickable = isMarkBtnClickable(),
        remainingTime = remainingTime().toString(),
    )
}
