package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.FootPrintInfo
import com.woowacourse.friendogly.presentation.model.FootPrintInfoUiModel

fun FootPrintInfo.toPresentation(): FootPrintInfoUiModel {
    return FootPrintInfoUiModel(
        "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
        "땡이",
        "소형견",
        "암컷",
        11,
        "안녕하세요! 땡이에요~",
    )
}
