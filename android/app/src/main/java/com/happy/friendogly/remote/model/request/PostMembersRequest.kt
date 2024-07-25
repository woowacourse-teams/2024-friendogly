package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PostMembersRequest(
    val name: String,
    val email: String,
)
