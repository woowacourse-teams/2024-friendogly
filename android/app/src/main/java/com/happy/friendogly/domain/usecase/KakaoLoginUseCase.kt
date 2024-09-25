package com.happy.friendogly.domain.usecase

import android.content.Context
import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.KakaoAccessToken
import com.happy.friendogly.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val repository: KakaoLoginRepository,
) {
    suspend operator fun invoke(context: Context): DomainResult<KakaoAccessToken, DataError.Local> = repository.login(context = context)
}
