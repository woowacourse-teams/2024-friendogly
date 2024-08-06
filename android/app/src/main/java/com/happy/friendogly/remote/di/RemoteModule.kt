package com.happy.friendogly.remote.di

import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.remote.api.BaseUrl
import com.happy.friendogly.remote.api.ClubService
import com.happy.friendogly.remote.api.FootprintService
import com.happy.friendogly.remote.api.MemberService
import com.happy.friendogly.remote.api.PetService
import com.happy.friendogly.remote.api.WoofService
import com.happy.friendogly.remote.interceptor.AuthorizationInterceptor
import com.happy.friendogly.remote.interceptor.ErrorResponseInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RemoteModule {
    private val contentType = "application/json".toMediaType()
    private val logging =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    fun createClubService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
    ): ClubService {
        return createRetrofit(
            baseUrl,
            tokenManager,
        ).create(ClubService::class.java)
    }

    fun createFootprintService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
    ): FootprintService {
        return createRetrofit(
            baseUrl,
            tokenManager,
        ).create(FootprintService::class.java)
    }

    fun createWoofService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
    ): WoofService {
        return createRetrofit(
            baseUrl,
            tokenManager,
        ).create(WoofService::class.java)
    }

    fun createMemberService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
    ): MemberService {
        return createRetrofit(
            baseUrl,
            tokenManager,
        ).create(MemberService::class.java)
    }

    fun createPetService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
    ): PetService {
        return createRetrofit(
            baseUrl,
            tokenManager,
        ).create(PetService::class.java)
    }

    private val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

    private fun createRetrofit(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(AuthorizationInterceptor(tokenManager = tokenManager))
                addInterceptor(ErrorResponseInterceptor())
                addInterceptor(logging)
            },
        ).addConverterFactory(json.asConverterFactory(contentType)).build()
    }

    private fun createOkHttpClient(interceptors: OkHttpClient.Builder.() -> Unit = { }): OkHttpClient =
        OkHttpClient.Builder().apply(interceptors).build()
}
