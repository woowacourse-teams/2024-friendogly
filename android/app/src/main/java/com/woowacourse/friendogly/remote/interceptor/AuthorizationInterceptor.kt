package com.woowacourse.friendogly.remote.interceptor

import com.woowacourse.friendogly.local.di.LocalModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(
    private val localModule: LocalModule,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken =
            runBlocking {
                localModule.accessToken.first()
            }
        val request = from(chain.request(), accessToken)
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"

        fun from(
            request: Request,
            credentials: String,
        ): Request =
            request.newBuilder()
                .removeHeader(AUTHORIZATION)
                .addHeader(AUTHORIZATION, "1")
//                .addHeader(AUTHORIZATION, credentials)
                .build()
    }
}
