package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.LandMarkDto
import com.happy.friendogly.domain.model.LandMark

fun List<LandMarkDto>.toDomain(): List<LandMark> {
    return map { landMarkDto ->
        LandMark()
    }
}
