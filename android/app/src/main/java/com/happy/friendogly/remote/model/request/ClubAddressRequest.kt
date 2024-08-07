package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ClubAddressRequest(
    val province: String,
    val city: String,
    val village: String,
)
