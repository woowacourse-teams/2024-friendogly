package com.woowacourse.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class FootprintInfoResponse(
    val memberName: String,
    val petName: String,
    val petDescription: String,
    val petBirthDate: String,
    val petSizeType: String,
    val petGender: String,
    val footprintImageUrl: String,
    val createdAt: String,
    val isMine: Boolean,
)
