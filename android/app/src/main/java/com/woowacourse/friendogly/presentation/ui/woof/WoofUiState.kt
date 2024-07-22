package com.woowacourse.friendogly.presentation.ui.woof

import com.woowacourse.friendogly.presentation.model.FootprintMarkBtnInfoUiModel
import com.woowacourse.friendogly.presentation.model.FootprintUiModel
import com.woowacourse.friendogly.presentation.model.LandMarkUiModel

data class WoofUiState(
    val nearFootPrints: List<FootprintUiModel> = emptyList(),
    val landMarks: List<LandMarkUiModel> = emptyList(),
    val footPrintMarkBtnInfo: FootprintMarkBtnInfoUiModel? = null,
)
