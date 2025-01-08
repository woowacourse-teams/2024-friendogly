package com.happy.friendogly.domain.model

data class Register(
    val id: Long,
    val name: String,
    val tag: String = "",
    val imageUrl: String,
    val tokens: JwtToken,
)
