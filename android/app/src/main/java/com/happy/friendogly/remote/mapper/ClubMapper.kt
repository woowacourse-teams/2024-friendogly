package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.remote.model.response.ClubResponse
import com.happy.friendogly.remote.model.response.ClubSearchingResponse

fun ClubSearchingResponse.toData(): List<ClubDto> {
    return contents.map { it.toData() }
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
        allowedGender = allowedGender.map { it.toData() },
        allowedSize = allowedSize.map { it.toData() },
        memberCapacity = memberCapacity,
        currentMemberCount = currentMemberCount,
        imageUrl = imageUrl,
        petImageUrls = petImageUrls,
    )
}
