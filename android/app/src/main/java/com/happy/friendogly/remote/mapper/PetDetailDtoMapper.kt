package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PlaygroundPetDetailDto
import com.happy.friendogly.remote.model.response.PlaygroundPetDetailResponse

fun PlaygroundPetDetailResponse.toData(): PlaygroundPetDetailDto {
    return PlaygroundPetDetailDto(
        memberId = memberId,
        petId = petId,
        name = name,
        birthDate = birthDate,
        sizeType = sizeType.toData(),
        gender = gender.toData(),
        imageUrl = imageUrl,
        message = message,
        isArrival = isArrival,
        isMine = isMine,
    )
}

fun List<PlaygroundPetDetailResponse>.toData(): List<PlaygroundPetDetailDto> {
    return map { response ->
        response.toData()
    }
}
