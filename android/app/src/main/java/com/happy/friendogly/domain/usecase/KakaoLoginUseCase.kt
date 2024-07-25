package com.happy.friendogly.domain.usecase

import android.content.Context
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.repository.KakaoLoginRepository

class KakaoLoginUseCase(
    private val repository: KakaoLoginRepository,
) {
    suspend operator fun invoke(context: Context): Result<KakaoAccessToken> = repository.login(context = context)
}
