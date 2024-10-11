package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundJoinDto
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundJoin

fun PlaygroundJoinDto.toDomain(): PlaygroundJoin {
    return PlaygroundJoin(
        playgroundId = playgroundId,
    )
}
