package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PostLoginRequest(
    val accessToken: String,
)
