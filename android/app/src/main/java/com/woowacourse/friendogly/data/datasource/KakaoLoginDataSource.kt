package com.woowacourse.friendogly.data.datasource

import android.content.Context
import com.woowacourse.friendogly.data.model.KakaoAccessTokenDto

interface KakaoLoginDataSource {
    suspend fun login(context: Context): Result<KakaoAccessTokenDto>

    companion object {
        private var instance: KakaoLoginDataSource? = null

        fun setInstance(dataSource: KakaoLoginDataSource) {
            instance = dataSource
        }

        fun getInstance(): KakaoLoginDataSource = requireNotNull(instance)
    }
}
