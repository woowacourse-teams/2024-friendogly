package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PostClubRequest(
    val title: String,
    val content: String,
    val province: String,
    val city: String?,
    val village: String?,
    val allowedGenders: List<String>,
    val allowedSizes: List<String>,
    val memberCapacity: Int,
    val participatingPetsId: List<Long>,
)
