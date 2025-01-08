package com.happy.friendogly.local.mapper

import com.happy.friendogly.data.model.JwtTokenDto
import com.happy.friendogly.local.model.JwtTokenEntity

fun JwtTokenEntity.toData(): JwtTokenDto {
    return JwtTokenDto(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
}
