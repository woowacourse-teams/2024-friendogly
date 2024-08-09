package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequest(
    val content: String,
)
