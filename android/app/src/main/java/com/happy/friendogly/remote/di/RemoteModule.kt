package com.happy.friendogly.remote.di

import com.happy.friendogly.local.di.LocalModule
import com.happy.friendogly.remote.api.BaseUrl
import com.happy.friendogly.remote.api.ChatService
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
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import retrofit2.Retrofit
import java.time.Duration

object RemoteModule {
    private val contentType = "application/json".toMediaType()
    private val logging =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private const val TIME_OUT_MINUTE = 1L
    private const val PINT_OUT_SECOND = 100L


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

    fun createChatService(
        baseUrl: BaseUrl,
        localModule: LocalModule,
    ): ChatService {
        return createRetrofit(
            baseUrl,
            localModule,
        ).create(ChatService::class.java)
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
        OkHttpClient.Builder().callTimeout(Duration.ofMinutes(TIME_OUT_MINUTE))
            .pingInterval(Duration.ofSeconds(PINT_OUT_SECOND)).apply(interceptors).build()

    fun createStumpClient(
        localModule: LocalModule,
    ): StompClient {
        return createOkHttpClient {
            addInterceptor(AuthorizationInterceptor(localModule = localModule))
            addInterceptor(ErrorResponseInterceptor())
            addInterceptor(logging)
        }.let(::OkHttpWebSocketClient).let(::StompClient)
    }

}
