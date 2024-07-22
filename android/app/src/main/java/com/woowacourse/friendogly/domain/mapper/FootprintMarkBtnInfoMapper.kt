package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.presentation.model.FootprintMarkBtnInfoUiModel

fun FootprintMarkBtnInfo.toPresentation(): FootprintMarkBtnInfoUiModel {
    return FootprintMarkBtnInfoUiModel(
        isClickable = isMarkBtnClickable(),
        remainingTime = remainingTime(),
    )
}
