package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PostClubRequest(
    val title: String,
    val content: String,
    val address: String,
    val allowedGender: List<String>,
    val allowedSize: List<String>,
    val memberCapacity: Int,
    val imageUrl: String,
    val petIds: List<Long>,
)
