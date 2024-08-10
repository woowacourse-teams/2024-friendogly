package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class WalkStatusRequest(
    val latitude: Double,
    val longitude: Double,
)
