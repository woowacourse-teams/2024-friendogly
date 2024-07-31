package com.happy.friendogly.presentation.ui.woof

import com.happy.friendogly.presentation.ui.woof.model.FootprintSave
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintMarkBtnInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintUiModel

data class WoofUiState(
    val footprintSave: FootprintSave? = null,
    val footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel? = null,
    val nearFootprints: List<FootprintUiModel> = emptyList(),
)
