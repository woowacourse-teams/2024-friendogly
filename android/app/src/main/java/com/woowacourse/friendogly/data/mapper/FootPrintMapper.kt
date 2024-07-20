package com.woowacourse.friendogly.data.mapper

import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.domain.model.FootPrint

fun List<FootPrintDto>.toDomain(): List<FootPrint> {
    return map { footPrintDto ->
        FootPrint(
            footPrintId = footPrintDto.footPrintId,
            latLng = LatLng(footPrintDto.latitude, footPrintDto.longitude),
            createdAt = footPrintDto.createdAt,
            isMine = footPrintDto.isMine,
        )
    }
}
