package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.LandMark
import com.happy.friendogly.presentation.model.LandMarkUiModel

fun List<LandMark>.toPresentation(): List<LandMarkUiModel> {
    return map { domain ->
        LandMarkUiModel()
    }
}
