package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubPetDto
import com.happy.friendogly.domain.model.ClubPet

fun ClubPetDto.toDomain(): ClubPet {
    return ClubPet(
        id = id,
        name = name,
        imageUrl = imageUrl,
        isMine = isMine,
    )
}

fun ClubPet.toData(): ClubPetDto {
    return ClubPetDto(
        id = id,
        name = name,
        imageUrl = imageUrl,
        isMine = isMine,
    )
}
