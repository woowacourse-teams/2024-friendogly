package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val isRegistered: Boolean,
    val tokens: JwtTokenResponse?,
)
