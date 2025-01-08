package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubAddressResponse(
    val province: String,
    val city: String,
    val village: String,
)
