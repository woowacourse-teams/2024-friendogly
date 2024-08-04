package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubDetailDto
import com.happy.friendogly.domain.model.ClubDetail

fun ClubDetailDto.toDomain(): ClubDetail {
    return ClubDetail(
        id = id,
        title = title,
        content = title,
        allowedGender = allowedGender.map { it.toDomain() },
        allowedSize = allowedSize.map { it.toDomain() },
        ownerMemberName = ownerMemberName,
        ownerImageUrl = ownerImageUrl,
        address = address.toDomain(),
        isMine = isMine,
        alreadyParticipate = alreadyParticipate,
        canParticipate = canParticipate,
        memberDetails = memberDetails.map { it.toDomain() },
        petDetails = petDetails.map { it.toDomain() },
        createdAt = createdAt,
        currentMemberCount = currentMemberCount,
        imageUrl = imageUrl,
        memberCapacity = memberCapacity,
        status = status.toDomain()
    )
}

fun ClubDetail.toData(): ClubDetailDto {
    return ClubDetailDto(
        id = id,
        title = title,
        content = title,
        allowedGender = allowedGender.map { it.toData() },
        allowedSize = allowedSize.map { it.toData() },
        ownerMemberName = ownerMemberName,
        ownerImageUrl = ownerImageUrl,
        address = address.toData(),
        isMine = isMine,
        alreadyParticipate = alreadyParticipate,
        canParticipate = canParticipate,
        memberDetails = memberDetails.map { it.toData() },
        petDetails = petDetails.map { it.toData() },
        createdAt = createdAt,
        currentMemberCount = currentMemberCount,
        imageUrl = imageUrl,
        memberCapacity = memberCapacity,
        status = status.toData()
    )
}

