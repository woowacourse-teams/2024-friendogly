package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.WalkStatusDto
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus

fun WalkStatusDto.toDomain(): WalkStatus {
    return when (this) {
        WalkStatusDto.BEFORE -> WalkStatus.BEFORE
        WalkStatusDto.ONGOING -> WalkStatus.ONGOING
        WalkStatusDto.AFTER -> WalkStatus.AFTER
    }
}
