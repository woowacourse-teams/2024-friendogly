package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.ImageUpdateTypeDto
import com.happy.friendogly.remote.model.request.ImageUpdateTypeRequest

fun ImageUpdateTypeDto.toRemote(): ImageUpdateTypeRequest {
    return when (this) {
        ImageUpdateTypeDto.UPDATE -> ImageUpdateTypeRequest.UPDATE
        ImageUpdateTypeDto.NOT_UPDATE -> ImageUpdateTypeRequest.NOT_UPDATE
        ImageUpdateTypeDto.DELETE -> ImageUpdateTypeRequest.DELETE
    }
}
