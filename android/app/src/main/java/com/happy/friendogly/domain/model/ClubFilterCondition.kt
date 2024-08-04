package com.happy.friendogly.domain.model

enum class ClubFilterCondition(
    filterConditionName: String,
){
    ALL("전체"),
    OPEN("모집중"),
    ABLE_TO_JOIN("참여 가능"),
}
