package com.woowacourse.friendogly.domain.repository

import android.content.Context
import com.woowacourse.friendogly.domain.model.KakaoAccessToken

interface KakaoLoginRepository {
    suspend fun login(context: Context): Result<KakaoAccessToken>

    companion object {
        private var instance: KakaoLoginRepository? = null

        fun setInstance(repository: KakaoLoginRepository) {
            instance = repository
        }

        fun getInstance(): KakaoLoginRepository = requireNotNull(instance)
    }
}
