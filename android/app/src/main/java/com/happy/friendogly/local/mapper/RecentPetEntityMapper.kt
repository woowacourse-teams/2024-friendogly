package com.happy.friendogly.local.mapper

import com.happy.friendogly.data.model.RecentPetDto
import com.happy.friendogly.local.model.RecentPetEntity

fun RecentPetEntity.toData(): RecentPetDto =
    RecentPetDto(
        memberId = id,
        name = name,
        imgUrl = imgUrl,
    )
