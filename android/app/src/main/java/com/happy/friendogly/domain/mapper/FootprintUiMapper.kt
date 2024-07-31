package com.happy.friendogly.domain.mapper

import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintUiModel

fun Footprint.toPresentation(): FootprintUiModel {
    return FootprintUiModel(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isMine = isMine,
    )
}

fun List<Footprint>.toPresentation(): List<FootprintUiModel> {
    return map { domain ->
        domain.toPresentation()
    }
}
