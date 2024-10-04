package com.happy.friendogly.local.mapper

import com.happy.friendogly.data.model.SizeTypeDto
import com.happy.friendogly.local.model.SizeTypeEntity

fun SizeTypeEntity.toData(): SizeTypeDto {
    return when (this) {
        SizeTypeEntity.SMALL -> SizeTypeDto.SMALL
        SizeTypeEntity.MEDIUM -> SizeTypeDto.MEDIUM
        SizeTypeEntity.LARGE -> SizeTypeDto.LARGE
    }
}
