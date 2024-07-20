package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootPrintInfoDto
import com.woowacourse.friendogly.domain.model.FootPrintInfo
import java.time.LocalDate

fun FootPrintInfoDto.toDomain(): FootPrintInfo {
    return FootPrintInfo(
        "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
        "땡이",
        "소형견",
        "암컷",
        false,
        LocalDate.of(2020, 2, 22),
        "안녕하세요! 땡이에요~",
    )
}
