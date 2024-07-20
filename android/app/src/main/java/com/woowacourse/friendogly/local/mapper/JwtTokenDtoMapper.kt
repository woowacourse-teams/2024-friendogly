package com.woowacourse.friendogly.local.mapper

import com.woowacourse.friendogly.data.model.JwtTokenDto
import com.woowacourse.friendogly.local.model.JwtTokenEntity

fun JwtTokenEntity.toData(): JwtTokenDto {
    return JwtTokenDto(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
}
