package com.woowacourse.friendogly.domain.repository

import android.content.Context
import com.woowacourse.friendogly.domain.model.KakaoAccessToken

interface KakaoLoginRepository {
    suspend fun login(context: Context): Result<KakaoAccessToken>
}
