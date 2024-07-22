package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootprintMarkBtnInfoDto
import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun FootprintMarkBtnInfoDto.toDomain(): FootprintMarkBtnInfo {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(createdAt, formatter)
    return FootprintMarkBtnInfo(createdAt = localDateTime)
}
