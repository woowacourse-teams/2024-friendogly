package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootPrintMineLatest
import com.woowacourse.friendogly.presentation.model.MarkFootPrintBtnUiModel

fun FootPrintMineLatest.toPresentation(): MarkFootPrintBtnUiModel {
    return MarkFootPrintBtnUiModel(
        isClickable = isMarkBtnClickable(),
        remainingTime = remainingTime(),
    )
}
