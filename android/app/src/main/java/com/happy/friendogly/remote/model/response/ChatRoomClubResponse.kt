package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomClubResponse(
    val clubId: Long,
    val allowedSizeTypes: List<SizeTypeResponse>,
    val allowedGenders: List<GenderResponse>
)
