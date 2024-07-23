package com.woowacourse.friendogly.domain.mapper

import com.woowacourse.friendogly.domain.model.Footprint
import com.woowacourse.friendogly.presentation.model.FootprintUiModel

fun Footprint.toPresentation(): FootprintUiModel {
    return FootprintUiModel(
        footprintId = footprintId,
        latitude = latitude,
        longitude = longitude,
        isVisible = isVisible(),
        isMine = isMine,
        imageUrl = imageUrl,
    )
}

fun List<Footprint>.toPresentation(): List<FootprintUiModel> {
    return map { domain ->
        domain.toPresentation()
    }
}
