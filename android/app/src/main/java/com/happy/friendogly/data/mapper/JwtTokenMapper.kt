package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.JwtTokenDto
import com.happy.friendogly.domain.model.JwtToken

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
