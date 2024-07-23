package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.PetsDto
import com.woowacourse.friendogly.remote.model.response.PetsResponse

fun PetsResponse.toData(): PetsDto {
    return PetsDto(
        contents =
            this.contents.map { petResponse ->
                petResponse.toData()
            },
    )
}
