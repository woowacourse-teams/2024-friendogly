package com.happy.friendogly.domain.model

data class Member(
    val id: Long,
    val name: String,
    val tag: String,
    val email: String,
    val imageUrl: String,
)
