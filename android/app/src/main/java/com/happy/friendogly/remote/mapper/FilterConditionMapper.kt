package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.FilterConditionDto
import com.happy.friendogly.remote.model.request.FilterConditionRequest

fun FilterConditionDto.toRemote() : FilterConditionRequest{
    return when(this){
        FilterConditionDto.ALL -> FilterConditionRequest.ALL
        FilterConditionDto.OPEN -> FilterConditionRequest.OPEN
        FilterConditionDto.ABLE_TO_JOIN -> FilterConditionRequest.ABLE_TO_JOIN
    }
}

fun FilterConditionRequest.toData() : FilterConditionDto{
    return when(this){
        FilterConditionRequest.ALL -> FilterConditionDto.ALL
        FilterConditionRequest.OPEN -> FilterConditionDto.OPEN
        FilterConditionRequest.ABLE_TO_JOIN -> FilterConditionDto.ABLE_TO_JOIN
    }
}
