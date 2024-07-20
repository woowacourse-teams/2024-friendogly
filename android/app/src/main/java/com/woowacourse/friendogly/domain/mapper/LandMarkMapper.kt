package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.LandMark
import com.woowacourse.friendogly.presentation.model.LandMarkUiModel

fun List<LandMark>.toUiModel(): List<LandMarkUiModel> {
    return map { landMark ->
        LandMarkUiModel()
    }
}
