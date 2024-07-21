package com.woowacourse.friendogly.presentation.ui.woof

import com.woowacourse.friendogly.presentation.model.FootPrintUiModel
import com.woowacourse.friendogly.presentation.model.LandMarkUiModel
import com.woowacourse.friendogly.presentation.model.MarkFootPrintBtnUiModel

data class WoofUiState(
    val nearFootPrints: List<FootPrintUiModel> = emptyList(),
    val landMarks: List<LandMarkUiModel> = emptyList(),
    val markFootPrintBtn: MarkFootPrintBtnUiModel? = null,
)
