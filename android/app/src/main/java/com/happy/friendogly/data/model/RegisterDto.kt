package com.happy.friendogly.data.model

data class RegisterDto(
    val id: Long,
    val name: String,
    val tag: String = "",
    val imageUrl: String,
    val tokens: JwtTokenDto,
)
