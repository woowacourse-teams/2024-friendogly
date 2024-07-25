package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.PetsDto
import com.happy.friendogly.remote.model.response.PetsResponse

fun PetsResponse.toData(): PetsDto {
    return PetsDto(
        contents =
            this.contents.map { petResponse ->
                petResponse.toData()
            },
    )
}
