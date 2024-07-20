package com.woowacourse.friendogly.domain.model

data class JwtToken(
    val accessToken: String?,
    val refreshToken: String?,
)
