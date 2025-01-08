package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
enum class ClubStateResponse {
    OPEN,
    CLOSED,
    FULL,
}
