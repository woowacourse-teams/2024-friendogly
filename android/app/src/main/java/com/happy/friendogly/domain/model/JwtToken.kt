package com.happy.friendogly.domain.model

data class JwtToken(
    val accessToken: String?,
    val refreshToken: String?,
)
