package com.happy.friendogly.domain.repository

import android.content.Context
import com.happy.friendogly.domain.model.KakaoAccessToken

interface KakaoLoginRepository {
    suspend fun login(context: Context): Result<KakaoAccessToken>
}
