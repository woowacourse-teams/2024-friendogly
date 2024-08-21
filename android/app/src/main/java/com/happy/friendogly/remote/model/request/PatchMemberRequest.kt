package com.happy.friendogly.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchMemberRequest(
    val name: String,
    val imageUpdateType: ImageUpdateTypeRequest,
)
