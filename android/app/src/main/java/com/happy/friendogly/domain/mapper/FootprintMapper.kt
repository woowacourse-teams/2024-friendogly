package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.Footprint
import com.happy.friendogly.presentation.model.FootprintUiModel

fun Footprint.toPresentation(): FootprintUiModel {
    return FootprintUiModel(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        isMine = isMine,
        imageUrl = imageUrl,
    )
}

fun List<Footprint>.toPresentation(): List<FootprintUiModel> {
    return map { domain ->
        domain.toPresentation()
    }
}
