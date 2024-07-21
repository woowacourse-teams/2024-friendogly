package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.presentation.model.FootPrintUiModel

fun FootPrint.toUiModel(): FootPrintUiModel {
    return FootPrintUiModel(
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
    )
}

fun List<FootPrint>.toUiModel(): List<FootPrintUiModel> {
    return map { footPrint ->
        footPrint.toUiModel()
    }
}
