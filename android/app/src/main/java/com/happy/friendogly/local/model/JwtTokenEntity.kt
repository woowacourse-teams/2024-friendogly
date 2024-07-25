package com.happy.friendogly.local.model

data class JwtTokenEntity(
    val accessToken: String?,
    val refreshToken: String?,
)
