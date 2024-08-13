package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.WalkStatusDto
import com.happy.friendogly.remote.model.response.FootprintWalkStatusResponse
import com.happy.friendogly.remote.model.response.WalkStatusResponse

fun FootprintWalkStatusResponse.toData(): WalkStatusDto {
    return when (walkStatus) {
        WalkStatusResponse.BEFORE -> WalkStatusDto.BEFORE
        WalkStatusResponse.ONGOING -> WalkStatusDto.ONGOING
        WalkStatusResponse.AFTER -> WalkStatusDto.AFTER
    }
}
