package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundDto
import com.happy.friendogly.presentation.ui.woof.model.Playground

fun PlaygroundDto.toDomain(): Playground {
    return Playground(
        id = id,
        latitude = latitude,
        longitude = longitude,
        isParticipated = isParticipated,
    )
}

fun List<PlaygroundDto>.toDomain(): List<Playground> {
    return map { dto ->
        dto.toDomain()
    }
}
