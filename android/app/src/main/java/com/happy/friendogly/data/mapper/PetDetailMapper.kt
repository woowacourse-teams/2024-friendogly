package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PlaygroundPetDetailDto
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundPetDetail

fun PlaygroundPetDetailDto.toDomain(): PlaygroundPetDetail {
    return PlaygroundPetDetail(
        memberId = memberId,
        petId = petId,
        name = name,
        birthDate = birthDate,
        sizeType = sizeType.toDomain(),
        gender = gender.toDomain(),
        imageUrl = imageUrl,
        message = message,
        isArrival = isArrival,
        isMine = isMine,
    )
}

fun List<PlaygroundPetDetailDto>.toDomain(): List<PlaygroundPetDetail> {
    return map { dto ->
        dto.toDomain()
    }
}
