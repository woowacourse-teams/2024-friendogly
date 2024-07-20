package com.woowacourse.friendogly.presentation.ui.woof

import com.woowacourse.friendogly.presentation.model.FootPrintUiModel
import com.woowacourse.friendogly.presentation.model.LandMarkUiModel

data class WoofUiState(
    val nearFootPrints: List<FootPrintUiModel> = emptyList(),
    val landMarks: List<LandMarkUiModel> = emptyList(),
)
