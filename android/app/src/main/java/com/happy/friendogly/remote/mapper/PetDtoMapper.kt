package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PetDto
import com.happy.friendogly.remote.model.response.PetResponse

fun PetResponse.toData(): PetDto {
    return PetDto(
        id = this.id,
        memberId = this.memberId,
        name = this.name,
        description = this.description,
        birthDate = this.birthDate,
        sizeType = this.sizeType.toData(),
        gender = this.gender.toData(),
        imageUrl = this.imageUrl,
    )
}
