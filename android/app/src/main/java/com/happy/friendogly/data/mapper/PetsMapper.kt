package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.PetsDto
import com.happy.friendogly.domain.model.Pets

fun PetsDto.toDomain(): Pets {
    return Pets(
        contents =
            this.contents.map { petResponse ->
                petResponse.toDomain()
            },
    )
}
