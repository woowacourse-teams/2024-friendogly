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
            val responseBody = response.body

            if (response.isSuccessful) return response

            val errorResponse = responseBody?.string()?.let { createErrorResponse(it) }
            val apiExceptionResponse = createApiException(response.code, errorResponse)
            apiExceptionResponse?.let { throw it }

            return response
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
