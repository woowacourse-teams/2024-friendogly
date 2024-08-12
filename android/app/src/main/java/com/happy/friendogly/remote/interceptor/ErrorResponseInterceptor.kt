package com.happy.friendogly.remote.interceptor

import com.happy.friendogly.remote.error.createApiException
import com.happy.friendogly.remote.error.createErrorResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.net.ssl.SSLHandshakeException

class ErrorResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        try {
            val response = chain.proceed(request)
            if (response.isSuccessful) return response

            val responseBody = response.body?.string()
            val errorResponse = responseBody?.let { createErrorResponse(it) } ?: return response

            throw createApiException(response.code, errorResponse)
        } catch (e: Throwable) {
            when (e) {
                is IOException,
                is SSLHandshakeException,
                -> throw e

                else -> throw IOException(e)
            }
        }
    }
}
