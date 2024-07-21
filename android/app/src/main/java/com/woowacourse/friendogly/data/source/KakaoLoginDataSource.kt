package com.woowacourse.friendogly.data.source

import android.content.Context
import com.woowacourse.friendogly.data.model.KakaoAccessTokenDto

interface KakaoLoginDataSource {
    suspend fun login(context: Context): Result<KakaoAccessTokenDto>
}
