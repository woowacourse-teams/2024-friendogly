package com.happy.friendogly.remote.interceptor

import android.util.Log
import com.happy.friendogly.local.di.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(
    private val tokenManager: TokenManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken =
            runBlocking {
                tokenManager.accessToken.first()
            }
        Log.d("ttt accessToken", accessToken.toString())
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
                .addHeader(AUTHORIZATION, credentials)
                .build()
    }
}
