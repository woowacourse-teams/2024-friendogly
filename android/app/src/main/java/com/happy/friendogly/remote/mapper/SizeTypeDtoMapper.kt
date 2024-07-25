package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.SizeTypeDto
import com.happy.friendogly.remote.model.request.SizeTypeRequest
import com.happy.friendogly.remote.model.response.SizeTypeResponse

fun SizeTypeResponse.toData(): SizeTypeDto {
    return when (this) {
        SizeTypeResponse.SMALL -> SizeTypeDto.SMALL
        SizeTypeResponse.MEDIUM -> SizeTypeDto.MEDIUM
        SizeTypeResponse.LARGE -> SizeTypeDto.LARGE
    }
}

fun SizeTypeDto.toRemote(): SizeTypeRequest {
    return when (this) {
        SizeTypeDto.SMALL -> SizeTypeRequest.SMALL
        SizeTypeDto.MEDIUM -> SizeTypeRequest.MEDIUM
        SizeTypeDto.LARGE -> SizeTypeRequest.LARGE
    }
}
