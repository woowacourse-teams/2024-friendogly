package com.happy.friendogly.remote.model.response

data class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
