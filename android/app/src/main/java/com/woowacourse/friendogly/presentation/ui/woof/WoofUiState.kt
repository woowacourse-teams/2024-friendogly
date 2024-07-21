package com.woowacourse.friendogly.presentation.ui.woof

import com.woowacourse.friendogly.presentation.model.FootPrintMarkBtnInfoUiModel
import com.woowacourse.friendogly.presentation.model.FootPrintUiModel
import com.woowacourse.friendogly.presentation.model.LandMarkUiModel

data class WoofUiState(
    val nearFootPrints: List<FootPrintUiModel> = emptyList(),
    val landMarks: List<LandMarkUiModel> = emptyList(),
    val footPrintMarkBtnInfo: FootPrintMarkBtnInfoUiModel? = null,
)
