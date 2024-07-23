package com.woowacourse.friendogly.presentation.ui.woof

import com.woowacourse.friendogly.presentation.model.FootprintMarkBtnInfoUiModel
import com.woowacourse.friendogly.presentation.model.FootprintUiModel
import com.woowacourse.friendogly.presentation.model.LandMarkUiModel

data class WoofUiState(
    val footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel? = null,
    val nearFootprints: List<FootprintUiModel> = emptyList(),
    val landMarks: List<LandMarkUiModel> = emptyList(),
)
