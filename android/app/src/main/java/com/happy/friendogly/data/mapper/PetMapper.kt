package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PetDto
import com.happy.friendogly.domain.model.Pet

fun PetDto.toDomain(): Pet {
    return Pet(
        id = this.id,
        memberId = this.memberId,
        name = this.name,
        description = this.description,
        birthDate = this.birthDate,
        sizeType = this.sizeType.toDomain(),
        gender = this.gender.toDomain(),
        imageUrl = this.imageUrl,
    )
}
