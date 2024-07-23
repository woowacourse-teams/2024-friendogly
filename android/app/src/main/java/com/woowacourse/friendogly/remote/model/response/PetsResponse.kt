package com.woowacourse.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PetsResponse(
    val contents: List<PetResponse>,
)
