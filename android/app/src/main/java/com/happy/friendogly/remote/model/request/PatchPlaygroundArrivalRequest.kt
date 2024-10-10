package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchPlaygroundArrivalRequest(
    val latitude: Double,
    val longitude: Double,
)
