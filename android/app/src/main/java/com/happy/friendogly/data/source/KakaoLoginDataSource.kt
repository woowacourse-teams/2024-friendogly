package com.happy.friendogly.data.source

import android.content.Context
import com.happy.friendogly.data.model.KakaoAccessTokenDto

interface KakaoLoginDataSource {
    suspend fun login(context: Context): Result<KakaoAccessTokenDto>
}
