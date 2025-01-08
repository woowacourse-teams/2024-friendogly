package com.happy.friendogly.local.mapper

import com.happy.friendogly.data.model.RecentPetDto
import com.happy.friendogly.local.model.RecentPetEntity

fun RecentPetEntity.toData(): RecentPetDto =
    RecentPetDto(
        memberId = memberId,
        petId = petId,
        name = name,
        imgUrl = imgUrl,
        birthday = birthday,
        gender = gender.toData(),
        sizeType = sizeType.toData(),
        createAt = createdAt,
    )
