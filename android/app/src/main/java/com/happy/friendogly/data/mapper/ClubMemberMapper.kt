package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubMemberDto
import com.happy.friendogly.domain.model.ClubMember

fun ClubMemberDto.toDomain(): ClubMember {
    return ClubMember(
        id = id,
        name = name,
        imageUrl = imageUrl?.ifEmpty { null },
    )
}

fun ClubMember.toData(): ClubMemberDto {
    return ClubMemberDto(
        id = id,
        name = name,
        imageUrl = imageUrl?.ifEmpty { null },
    )
}
