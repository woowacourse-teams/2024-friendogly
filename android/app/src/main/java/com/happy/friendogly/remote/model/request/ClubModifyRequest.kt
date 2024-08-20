package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
class ClubModifyRequest(
    val title: String,
    val content: String,
    val status: ClubStateRequest,
)
