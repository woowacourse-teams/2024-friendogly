package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MyPlaygroundResponse(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
)
