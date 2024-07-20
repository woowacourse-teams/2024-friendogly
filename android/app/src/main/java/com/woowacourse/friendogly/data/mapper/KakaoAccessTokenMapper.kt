package com.woowacourse.friendogly.data.mapper

import com.woowacourse.friendogly.data.model.KakaoAccessTokenDto
import com.woowacourse.friendogly.domain.model.KakaoAccessToken

fun KakaoAccessTokenDto.toDomain(): KakaoAccessToken {
    return KakaoAccessToken(
        idToken = this.idToken,
        accessToken = this.accessToken,
    )
}
