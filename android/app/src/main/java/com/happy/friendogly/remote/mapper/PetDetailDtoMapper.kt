package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PetDetailDto
import com.happy.friendogly.remote.model.response.PetDetailResponse

fun PetDetailResponse.toData(): PetDetailDto {
    return PetDetailDto(
        name = name,
        description = description,
        birthDate = birthDate,
        sizeType = sizeType.toData(),
        gender = gender.toData(),
        imageUrl = imageUrl,
    )
}

fun List<PetDetailResponse>.toData(): List<PetDetailDto> {
    return map { response ->
        response.toData()
    }
}
