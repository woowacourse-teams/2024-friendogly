package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.RecentPetDto
import com.happy.friendogly.domain.model.RecentPet
import com.happy.friendogly.local.model.RecentPetEntity

fun RecentPetDto.toLocal(): RecentPetEntity =
    RecentPetEntity(
        memberId = id,
        name = name,
        imgUrl = imgUrl,
    )

fun RecentPetDto.toDomain(): RecentPet =
    RecentPet(
        memberId = id,
        name = name,
        imgUrl = imgUrl,
    )
