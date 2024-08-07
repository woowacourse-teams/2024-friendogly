package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.MemberDto
import com.happy.friendogly.remote.model.response.MemberResponse

fun MemberResponse.toData(): MemberDto {
    return MemberDto(
        id = id,
        name = name,
        tag = tag,
        email = email,
        imageUrl = imageUrl,
    )
}
