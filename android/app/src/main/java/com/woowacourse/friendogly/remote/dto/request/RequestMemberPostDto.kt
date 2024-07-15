package com.woowacourse.friendogly.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestMemberPostDto(
    val name: String,
)
