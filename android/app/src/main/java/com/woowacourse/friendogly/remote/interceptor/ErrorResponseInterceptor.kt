package com.woowacourse.friendogly.remote.interceptor

import com.woowacourse.friendogly.remote.model.error.BadRequestException
import com.woowacourse.friendogly.remote.model.error.ErrorResponseImpl
import com.woowacourse.friendogly.remote.model.error.InternalServerErrorException
import com.woowacourse.friendogly.remote.model.error.InvalidAccessTokenException
import com.woowacourse.friendogly.remote.model.error.InvalidAccessTokenExpire
import com.woowacourse.friendogly.remote.model.error.ServerNotFoundException
import kotlinx.serialization.json.Json
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

            val requestUrl = request.url.toString()
            val errorResponse = responseBody?.string()?.let { createErrorResponse(it) }
            val errorException = createErrorException(requestUrl, response.code, errorResponse)
            errorException?.let { throw it }

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

fun createErrorResponse(responseBodyString: String): ErrorResponseImpl? =
    try {
        Json.decodeFromString<ErrorResponseImpl>(responseBodyString)
    } catch (e: Exception) {
        null
    }

fun createErrorException(
    url: String?,
    httpCode: Int,
    errorResponse: ErrorResponseImpl?,
): Exception? =
    when (httpCode) {
        400 -> BadRequestException(Throwable(errorResponse?.reason), url)
        401 -> InvalidAccessTokenExpire(Throwable(errorResponse?.reason), url)
        403 -> InvalidAccessTokenException(Throwable(errorResponse?.reason), url)
        404 -> ServerNotFoundException(Throwable(errorResponse?.reason), url)
        500 -> InternalServerErrorException(Throwable(errorResponse?.reason), url)
        else -> null
    }
