package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ClubFilterConditionDto
import com.happy.friendogly.remote.model.request.ClubFilterConditionRequest

fun ClubFilterConditionDto.toRemote() : ClubFilterConditionRequest{
    return when(this){
        ClubFilterConditionDto.ALL -> ClubFilterConditionRequest.ALL
        ClubFilterConditionDto.OPEN -> ClubFilterConditionRequest.OPEN
        ClubFilterConditionDto.ABLE_TO_JOIN -> ClubFilterConditionRequest.ABLE_TO_JOIN
    }
}

fun ClubFilterConditionRequest.toData() : ClubFilterConditionDto{
    return when(this){
        ClubFilterConditionRequest.ALL -> ClubFilterConditionDto.ALL
        ClubFilterConditionRequest.OPEN -> ClubFilterConditionDto.OPEN
        ClubFilterConditionRequest.ABLE_TO_JOIN -> ClubFilterConditionDto.ABLE_TO_JOIN
    }
}
