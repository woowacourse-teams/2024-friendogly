package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val id: Long,
    val name: String,
    val tag: String = "",
    val email: String,
    val imageUrl: String,
)
