package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.domain.model.Club

fun ClubDto.toDomain(): Club{
    return Club(
        id = id,
        title = title,
        content = content,
        ownerMemberName = ownerMemberName,
        address = address.toDomain(),
        status = status.toDomain(),
        createdAt = createdAt,
        allowedSize = allowedSize.map { it.toDomain() },
        allowedGender = allowedGender.map { it.toDomain() },
        memberCapacity = memberCapacity,
        imageUrl = imageUrl,
        currentMemberCount = currentMemberCount,
        petImageUrls = petImageUrls,
    )
}


fun Club.toData(): ClubDto {
    return ClubDto(
        id = id,
        title = title,
        content = content,
        ownerMemberName = ownerMemberName,
        address = address.toData(),
        status = status.toData(),
        createdAt = createdAt,
        allowedSize = allowedSize.map { it.toData() },
        allowedGender = allowedGender.map { it.toData() },
        memberCapacity = memberCapacity,
        imageUrl = imageUrl,
        currentMemberCount = currentMemberCount,
        petImageUrls = petImageUrls,
    )
}
