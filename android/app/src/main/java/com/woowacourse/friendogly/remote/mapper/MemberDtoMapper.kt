package com.woowacourse.friendogly.remote.mapper

import com.woowacourse.friendogly.data.model.MemberDto
import com.woowacourse.friendogly.remote.model.response.MemberResponse

fun MemberResponse.toData(): MemberDto {
    return MemberDto(
        id = id,
        name = name,
        tag = tag,
        email = email,
    )
}
