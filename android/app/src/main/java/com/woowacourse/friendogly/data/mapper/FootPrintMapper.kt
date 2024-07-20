package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.domain.model.FootPrint

fun List<FootPrintDto>.toDomain(): List<FootPrint> {
    return map { footPrintDto ->
        FootPrint()
    }
}
