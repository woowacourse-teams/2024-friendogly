package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.presentation.ui.playground.model.Playground

fun PlaygroundDto.toDomain(): Playground {
    return Playground(
        id = id,
        latitude = latitude,
        longitude = longitude,
        isParticipating = isParticipating,
    )
}

fun List<PlaygroundDto>.toDomain(): List<Playground> {
    return map { dto ->
        dto.toDomain()
    }
}
