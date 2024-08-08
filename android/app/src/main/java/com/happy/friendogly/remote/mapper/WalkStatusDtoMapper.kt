package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.WalkStatusDto
import com.happy.friendogly.remote.model.response.WalkStatusResponse

fun WalkStatusResponse.toData(): WalkStatusDto {
    return when (this) {
        WalkStatusResponse.BEFORE -> WalkStatusDto.BEFORE
        WalkStatusResponse.ONGOING -> WalkStatusDto.ONGOING
        WalkStatusResponse.AFTER -> WalkStatusDto.AFTER
    }
}
