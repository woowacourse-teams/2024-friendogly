package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ClubStateDto
import com.happy.friendogly.domain.model.ClubState

fun ClubStateDto.toDomain(): ClubState {
    return when (this) {
        ClubStateDto.OPEN -> ClubState.OPEN
        ClubStateDto.CLOSED -> ClubState.CLOSED
        ClubStateDto.FULL -> ClubState.FULL
    }
}

fun ClubState.toData(): ClubStateDto {
    return when (this) {
        ClubState.OPEN -> ClubStateDto.OPEN
        ClubState.CLOSED -> ClubStateDto.CLOSED
        ClubState.FULL -> ClubStateDto.FULL
    }
}
