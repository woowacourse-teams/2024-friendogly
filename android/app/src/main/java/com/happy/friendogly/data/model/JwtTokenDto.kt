package com.happy.friendogly.data.model

data class JwtTokenDto(
    val accessToken: String?,
    val refreshToken: String?,
)
