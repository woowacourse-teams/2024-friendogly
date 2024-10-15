package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SearchingClubResponse(
    val isLastPage : Boolean,
    val content: List<ClubResponse>,
)
