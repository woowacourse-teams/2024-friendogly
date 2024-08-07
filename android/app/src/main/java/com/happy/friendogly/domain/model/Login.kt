package com.happy.friendogly.domain.model

data class Login(
    val isRegistered: Boolean,
    val tokens: JwtToken?,
)
