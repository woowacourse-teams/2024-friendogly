package com.happy.friendogly.presentation.ui.woof

import com.happy.friendogly.domain.model.FootprintSave
import com.happy.friendogly.presentation.model.FootprintMarkBtnInfoUiModel
import com.happy.friendogly.presentation.model.FootprintUiModel
import com.happy.friendogly.presentation.model.LandMarkUiModel

data class WoofUiState(
    val footprintSave: FootprintSave? = null,
    val footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel? = null,
    val nearFootprints: List<FootprintUiModel> = emptyList(),
    val landMarks: List<LandMarkUiModel> = emptyList(),
)
