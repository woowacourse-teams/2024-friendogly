package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundArrivalRequest(
    val latitude: Double,
    val longitude: Double,
)
