package com.happy.friendogly.remote.model.request

class ClubModifyRequest(
    val title: String,
    val content: String,
    val status: ClubStateRequest,
)
