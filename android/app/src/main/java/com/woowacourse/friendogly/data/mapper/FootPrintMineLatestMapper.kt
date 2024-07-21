package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootPrintMineLatestDto
import com.woowacourse.friendogly.domain.model.FootPrintMineLatest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun FootPrintMineLatestDto.toDomain(): FootPrintMineLatest {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(createdAt, formatter)
    return FootPrintMineLatest(createdAt = localDateTime)
}
