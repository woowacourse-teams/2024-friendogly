package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.presentation.model.FootPrintUiModel

fun FootPrint.toPresentation(): FootPrintUiModel {
    return FootPrintUiModel(
        latitude = latitude,
        longitude = longitude,
        isVisible = isVisible(),
        isMine = isMine,
    )
}

fun List<FootPrint>.toPresentation(): List<FootPrintUiModel> {
    return map { domain ->
        domain.toPresentation()
    }
}
