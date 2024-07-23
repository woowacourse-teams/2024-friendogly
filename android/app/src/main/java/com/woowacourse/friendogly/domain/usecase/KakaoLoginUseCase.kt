package com.woowacourse.friendogly.domain.usecase

import android.content.Context
import com.woowacourse.friendogly.domain.model.KakaoAccessToken
import com.woowacourse.friendogly.domain.repository.KakaoLoginRepository

class KakaoLoginUseCase(
    private val repository: KakaoLoginRepository,
) {
    suspend operator fun invoke(context: Context): Result<KakaoAccessToken> = repository.login(context = context)
}
