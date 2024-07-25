package com.happy.friendogly.kakao.mapper

import com.happy.friendogly.data.model.KakaoAccessTokenDto
import com.happy.friendogly.kakao.model.KakaoAccessTokenResponse

fun KakaoAccessTokenResponse.toData(): KakaoAccessTokenDto {
    return KakaoAccessTokenDto(
        accessToken = this.accessToken,
        idToken = this.idToken,
    )
}
