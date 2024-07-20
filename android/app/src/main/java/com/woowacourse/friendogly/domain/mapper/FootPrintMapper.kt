package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootPrint
import com.woowacourse.friendogly.presentation.model.FootPrintUiModel

fun List<FootPrint>.toUiModel(): List<FootPrintUiModel> {
    return map { footPrint ->
        FootPrintUiModel()
    }
}
