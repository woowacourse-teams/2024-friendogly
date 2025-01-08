package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PetExistenceDto
import com.happy.friendogly.presentation.ui.playground.model.PetExistence

fun PetExistenceDto.toDomain(): PetExistence {
    return PetExistence(isExistPet = isExistPet)
}
