package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootPrintMarkBtnInfoDto
import com.woowacourse.friendogly.domain.model.FootPrintMarkBtnInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun FootPrintMarkBtnInfoDto.toDomain(): FootPrintMarkBtnInfo {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(createdAt, formatter)
    return FootPrintMarkBtnInfo(createdAt = localDateTime)
}
