package com.happy.friendogly.remote.di

import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.remote.api.AuthenticationListener
import com.happy.friendogly.remote.api.Authenticator
import com.happy.friendogly.remote.api.BaseUrl
import com.happy.friendogly.remote.api.ClubService
import com.happy.friendogly.remote.api.FootprintService
import com.happy.friendogly.remote.api.MemberService
import com.happy.friendogly.remote.api.PetService
import com.happy.friendogly.remote.api.RefreshService
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
        authenticationListener: AuthenticationListener,
    ): ClubService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(ClubService::class.java)
    }

    fun createFootprintService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): FootprintService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(FootprintService::class.java)
    }

    fun createWoofService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): WoofService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(WoofService::class.java)
    }

    fun createMemberService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): MemberService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(MemberService::class.java)
    }

    fun createPetService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): PetService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
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
        authenticationListener: AuthenticationListener,
    ): Retrofit {
        val authenticator =
            Authenticator(
                refreshService = createRefreshApiService(baseUrl),
                tokenManager = tokenManager,
                authenticationListener = authenticationListener,
            )

        return Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(AuthorizationInterceptor(tokenManager = tokenManager))
                authenticator(authenticator)
                addInterceptor(ErrorResponseInterceptor())
                addInterceptor(logging)
            },
        ).addConverterFactory(json.asConverterFactory(contentType)).build()
    }

    private fun createOkHttpClient(interceptors: OkHttpClient.Builder.() -> Unit = { }): OkHttpClient =
        OkHttpClient.Builder().apply(interceptors).build()

    private fun createRefreshApiService(baseUrl: BaseUrl): RefreshService =
        Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(ErrorResponseInterceptor())
                addInterceptor(logging)
            },
        ).addConverterFactory(json.asConverterFactory(contentType)).build()
            .create(RefreshService::class.java)
}
