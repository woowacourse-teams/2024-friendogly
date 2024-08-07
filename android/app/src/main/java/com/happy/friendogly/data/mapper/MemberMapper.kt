package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.MemberDto
import com.happy.friendogly.domain.model.Member

fun MemberDto.toDomain(): Member {
    return Member(
        id = id,
        name = name,
        tag = tag,
        email = email,
        imageUrl = imageUrl,
    )
}
