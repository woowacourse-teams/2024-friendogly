package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.remote.model.response.ClubResponse

fun List<ClubResponse>.toData(): List<ClubDto> {
    return this.map { it.toData() }
}

fun ClubResponse.toData(): ClubDto {
    return ClubDto(
        id = id,
        title = title,
        content = content,
        ownerMemberName = ownerMemberName,
        address = address.toData(),
        status = status.toData(),
        createdAt = createdAt,
        memberCapacity = memberCapacity,
        currentMemberCount = currentMemberCount,
        imageUrl = imageUrl,
        petImageUrls = petImageUrls,
    )
}
