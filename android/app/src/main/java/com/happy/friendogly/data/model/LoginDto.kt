package com.happy.friendogly.data.model

data class LoginDto(
    val isRegistered: Boolean,
    val tokens: JwtTokenDto?,
)
