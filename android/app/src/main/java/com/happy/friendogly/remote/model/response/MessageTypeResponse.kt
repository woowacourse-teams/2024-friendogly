package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
enum class MessageTypeResponse {
    ENTER,
    CHAT,
    LEAVE,
}
