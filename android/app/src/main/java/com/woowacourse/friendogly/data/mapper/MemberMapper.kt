package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.MemberDto
import com.woowacourse.friendogly.domain.model.Member

fun MemberDto.toDomain(): Member {
    return Member(
        id = id,
    )
}
