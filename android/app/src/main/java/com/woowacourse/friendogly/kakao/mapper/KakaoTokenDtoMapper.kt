package com.woowacourse.friendogly.kakao.mapper

import com.woowacourse.friendogly.data.model.KakaoAccessTokenDto
import com.woowacourse.friendogly.kakao.model.KakaoAccessTokenResponse

fun KakaoAccessTokenResponse.toData(): KakaoAccessTokenDto {
    return KakaoAccessTokenDto(
        accessToken = this.accessToken,
        idToken = this.idToken,
    )
}
