package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PetDetailDto
import com.happy.friendogly.presentation.ui.woof.model.PetDetail

fun PetDetailDto.toDomain(): PetDetail {
    return PetDetail(
        name = name,
        description = description,
        birthDate = birthDate,
        sizeType = sizeType.toDomain(),
        gender = gender.toDomain(),
        imageUrl = imageUrl,
    )
}

fun List<PetDetailDto>.toDomain(): List<PetDetail> {
    return map { dto ->
        dto.toDomain()
    }
}
