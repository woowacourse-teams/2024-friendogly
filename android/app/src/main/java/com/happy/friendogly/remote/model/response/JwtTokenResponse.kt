package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
