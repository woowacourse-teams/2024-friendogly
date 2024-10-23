package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PostPlaygroundRequest(
    val latitude: Double,
    val longitude: Double,
)
