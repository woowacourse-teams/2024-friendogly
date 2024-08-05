package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubFilterConditionDto
import com.happy.friendogly.domain.model.ClubFilterCondition

fun ClubFilterConditionDto.toDomain(): ClubFilterCondition {
    return when (this) {
        ClubFilterConditionDto.ALL -> ClubFilterCondition.ALL
        ClubFilterConditionDto.OPEN -> ClubFilterCondition.OPEN
        ClubFilterConditionDto.ABLE_TO_JOIN -> ClubFilterCondition.ABLE_TO_JOIN
    }
}

fun ClubFilterCondition.toData(): ClubFilterConditionDto {
    return when (this) {
        ClubFilterCondition.ALL -> ClubFilterConditionDto.ALL
        ClubFilterCondition.OPEN -> ClubFilterConditionDto.OPEN
        ClubFilterCondition.ABLE_TO_JOIN -> ClubFilterConditionDto.ABLE_TO_JOIN
    }
}
