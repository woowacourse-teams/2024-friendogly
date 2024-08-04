package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubSearchingResponse(
    val data: List<ClubResponse>
)
