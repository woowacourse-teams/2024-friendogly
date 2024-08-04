package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PostClubRequest(
    val title: String,
    val content: String,
    val address: ClubAddressRequest,
    val allowedGender: List<GenderRequest>,
    val allowedSize: List<SizeTypeRequest>,
    val memberCapacity: Int,
    val petIds: List<Long>,
)
