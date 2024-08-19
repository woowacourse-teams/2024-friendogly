package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubStateDto
import com.happy.friendogly.remote.model.request.ClubStateRequest
import com.happy.friendogly.remote.model.response.ClubStateResponse

fun ClubStateResponse.toData(): ClubStateDto {
    return when (this) {
        ClubStateResponse.OPEN -> ClubStateDto.OPEN
        ClubStateResponse.CLOSE -> ClubStateDto.CLOSE
        ClubStateResponse.FULL -> ClubStateDto.FULL
    }
}

fun ClubStateDto.toRemote(): ClubStateRequest {
    return when (this) {
        ClubStateDto.OPEN -> ClubStateRequest.OPEN
        ClubStateDto.CLOSE, ClubStateDto.FULL -> ClubStateRequest.CLOSE
    }
}
