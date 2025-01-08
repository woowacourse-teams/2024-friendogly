package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundArrivalDto
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundArrival

fun PlaygroundArrivalDto.toDomain(): PlaygroundArrival =
    PlaygroundArrival(
        isArrived = isArrived,
    )
