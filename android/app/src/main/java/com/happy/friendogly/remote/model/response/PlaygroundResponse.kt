package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PlaygroundResponse(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val isParticipating: Boolean,
)
