package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.KakaoAccessTokenDto
import com.happy.friendogly.domain.model.KakaoAccessToken

fun KakaoAccessTokenDto.toDomain(): KakaoAccessToken {
    return KakaoAccessToken(
        idToken = this.idToken,
        accessToken = this.accessToken,
    )
}
