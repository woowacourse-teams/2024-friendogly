package com.happy.friendogly.data.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.domain.model.Club

fun List<ClubDto>.toDomain(): List<Club> {
    return this.map { it.toDomain() }
}

fun PagingData<ClubDto>.toDomain(): PagingData<Club> {
    return this.map { it.toDomain() }
}

fun ClubDto.toDomain(): Club {
    return Club(
        id = id,
        title = title,
        content = content,
        ownerMemberName = ownerMemberName,
        address = address.toDomain(),
        status = status.toDomain(),
        createdAt = createdAt,
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
        memberCapacity = memberCapacity,
        imageUrl = imageUrl,
        currentMemberCount = currentMemberCount,
        petImageUrls = petImageUrls,
    )
}
