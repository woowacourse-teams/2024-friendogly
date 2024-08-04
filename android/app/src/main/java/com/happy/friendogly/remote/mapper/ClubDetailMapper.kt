package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubDetailDto
import com.happy.friendogly.data.model.ClubMemberDto
import com.happy.friendogly.data.model.ClubPetDto
import com.happy.friendogly.remote.model.response.ClubDetailResponse
import com.happy.friendogly.remote.model.response.ClubDetailMemberResponse
import com.happy.friendogly.remote.model.response.ClubDetailPetResponse

fun ClubDetailResponse.toData(): ClubDetailDto {
    return ClubDetailDto(
        id = id,
        title = title,
        content = content,
        ownerMemberName = ownerMemberName,
        address = address,
        status = status.toData(),
        createdAt = createdAt,
        allowedGender = allowedGender.map { it.toData() },
        allowedSize = allowedSize.map { it.toData() },
        memberCapacity = memberCapacity,
        currentMemberCount = currentMemberCount,
        imageUrl = imageUrl,
        ownerImageUrl = ownerImageUrl,
        isMine = isMine,
        alreadyParticipate = alreadyParticipate,
        canParticipate = canParticipate,
        memberDetails = memberDetails.map { it.toData() },
        petDetails = petDetails.map { it.toData() },
    )
}


fun ClubDetailMemberResponse.toData(): ClubMemberDto {
    return ClubMemberDto(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )
}

fun ClubDetailPetResponse.toData(): ClubPetDto {
    return ClubPetDto(
        id = id,
        name = name,
        imageUrl = imageUrl,
        isMine = isMine,
    )
}
