package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PostClubRequest(
    val title: String,
    val content: String,
    val province: String,
    val city: String,
    val village: String,
    @Serializable
    val allowedGender: List<GenderRequest>,
    @Serializable
    val allowedSize: List<SizeTypeRequest>,
    val memberCapacity: Int,
    val petIds: List<Long>,
)
