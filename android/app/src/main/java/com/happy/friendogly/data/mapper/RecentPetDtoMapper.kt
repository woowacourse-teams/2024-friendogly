package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.RecentPetDto
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.local.model.RecentPetEntity
import com.happy.friendogly.remote.mapper.toLocal

fun RecentPetDto.toLocal(): RecentPetEntity =
    RecentPetEntity(
        memberId = id,
        name = name,
        imgUrl = imgUrl,
        birthday = birthday,
        gender = gender.toLocal(),
        sizeType = sizeType.toLocal(),
        createdAt = createAt,
    )

fun RecentPetDto.toDomain(): RecentPet =
    RecentPet(
        memberId = id,
        name = name,
        imgUrl = imgUrl,
        birthday = birthday,
        sizeType = sizeType.toDomain(),
        gender = gender.toDomain(),
        createAt = createAt,
    )
