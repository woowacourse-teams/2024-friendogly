package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootPrintMarkBtnInfo
import com.woowacourse.friendogly.presentation.model.FootPrintMarkBtnInfoUiModel

fun FootPrintMarkBtnInfo.toPresentation(): FootPrintMarkBtnInfoUiModel {
    return FootPrintMarkBtnInfoUiModel(
        isClickable = isMarkBtnClickable(),
        remainingTime = remainingTime(),
    )
}
