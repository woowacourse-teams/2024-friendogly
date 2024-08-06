package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class FootprintWalkStatusResponse(
    val walkStatus: WalkStatusResponse,
)
