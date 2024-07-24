package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.PetsDto
import com.woowacourse.friendogly.domain.model.Pets

fun PetsDto.toDomain(): Pets {
    return Pets(
        contents =
            this.contents.map { petResponse ->
                petResponse.toDomain()
            },
    )
}
