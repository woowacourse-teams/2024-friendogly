package com.happy.friendogly.domain.model

enum class ClubState(
    val clubStateName: String,
) {
    OPEN("모집중"),
    CLOSE("모집종료"),
    FULL("모집완료"),
}
