package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.remote.model.response.PetExistenceResponse

fun PetExistenceResponse.toData(): PetExistenceDto {
    return PetExistenceDto(
        isExistPet = isExistPet,
    )
}
