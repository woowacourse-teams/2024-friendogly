package com.happy.friendogly.presentation.ui.woof

import com.happy.friendogly.domain.model.FootprintSave
import com.happy.friendogly.presentation.model.FootprintMarkBtnInfoUiModel
import com.happy.friendogly.presentation.model.FootprintUiModel

data class WoofUiState(
    val footprintSave: FootprintSave? = null,
    val footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel? = null,
    val nearFootprints: List<FootprintUiModel> = emptyList(),
)
