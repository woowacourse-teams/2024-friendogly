package com.woowacourse.friendogly.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.woowacourse.friendogly.local.di.LocalModule
import com.woowacourse.friendogly.remote.api.BaseUrl
import com.woowacourse.friendogly.remote.api.ClubService
import com.woowacourse.friendogly.remote.api.FootprintService
import com.woowacourse.friendogly.remote.interceptor.AuthorizationInterceptor
import com.woowacourse.friendogly.remote.interceptor.ErrorResponseInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RemoteModule {
    private val contentType = "application/json".toMediaType()

    fun createFootprintService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): FootprintService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(FootprintService::class.java)
    }

    fun createClubService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): ClubService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(ClubService::class.java)
    }

    private fun createRetrofit(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(AuthorizationInterceptor(localModule = localModule))
                addInterceptor(ErrorResponseInterceptor())
            },
        ).addConverterFactory(Json.asConverterFactory(contentType)).build()
    }

    private fun createOkHttpClient(interceptors: OkHttpClient.Builder.() -> Unit = { }): OkHttpClient =
        OkHttpClient.Builder().apply(interceptors).build()
}
