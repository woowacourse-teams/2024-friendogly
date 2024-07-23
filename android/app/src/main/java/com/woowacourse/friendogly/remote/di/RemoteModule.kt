package com.woowacourse.friendogly.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.woowacourse.friendogly.local.di.LocalModule
import com.woowacourse.friendogly.remote.api.BaseUrl
import com.woowacourse.friendogly.remote.api.ClubService
import com.woowacourse.friendogly.remote.api.FootprintService
import com.woowacourse.friendogly.remote.api.WoofService
import com.woowacourse.friendogly.remote.api.MemberService
import com.woowacourse.friendogly.remote.api.PetService
import com.woowacourse.friendogly.remote.interceptor.AuthorizationInterceptor
import com.woowacourse.friendogly.remote.interceptor.ErrorResponseInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RemoteModule {
    private val contentType = "application/json".toMediaType()

    fun createClubService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): ClubService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(ClubService::class.java)
    }

    fun createFootprintService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): FootprintService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(FootprintService::class.java)
    }

    fun createWoofService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): WoofService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(WoofService::class.java)
    }

    private val logging =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    fun createMemberService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): MemberService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(MemberService::class.java)
    }

    fun createPetService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): PetService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(PetService::class.java)
    }

    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

    private fun createRetrofit(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(AuthorizationInterceptor(localModule = localModule))
                addInterceptor(ErrorResponseInterceptor())
                addInterceptor(logging)
            },
        ).addConverterFactory(json.asConverterFactory(contentType)).build()
    }

    private fun createOkHttpClient(interceptors: OkHttpClient.Builder.() -> Unit = { }): OkHttpClient =
        OkHttpClient.Builder().apply(interceptors).build()
}
