package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeviceTokenRequest(
    val deviceToken: String,
)
