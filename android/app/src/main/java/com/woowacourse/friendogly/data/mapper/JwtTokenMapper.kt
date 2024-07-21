package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.JwtTokenDto
import com.woowacourse.friendogly.domain.model.JwtToken

fun JwtTokenDto.toDomain(): JwtToken {
    return JwtToken(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
}

fun JwtToken.toData(): JwtTokenDto {
    return JwtTokenDto(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
}
