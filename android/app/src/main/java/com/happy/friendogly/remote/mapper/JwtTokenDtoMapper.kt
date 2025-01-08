package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.JwtTokenDto
import com.happy.friendogly.remote.model.response.JwtTokenResponse

fun JwtTokenResponse.toData(): JwtTokenDto {
    return JwtTokenDto(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
}
