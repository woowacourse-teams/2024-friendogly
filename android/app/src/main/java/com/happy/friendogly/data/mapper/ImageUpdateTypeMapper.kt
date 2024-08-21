package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.ImageUpdateTypeDto
import com.happy.friendogly.domain.model.ImageUpdateType

fun ImageUpdateType.toData(): ImageUpdateTypeDto {
    return when (this) {
        ImageUpdateType.UPDATE -> ImageUpdateTypeDto.UPDATE
        ImageUpdateType.NOT_UPDATE -> ImageUpdateTypeDto.NOT_UPDATE
        ImageUpdateType.DELETE -> ImageUpdateTypeDto.DELETE
    }
}
