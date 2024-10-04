package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundRequest(
    val latitude: Double,
    val longitude: Double,
)
