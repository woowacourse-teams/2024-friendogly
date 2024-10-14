package com.happy.friendogly.remote.di

import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.remote.api.AlarmTokenService
import com.happy.friendogly.remote.api.AuthService
import com.happy.friendogly.remote.api.AuthenticationListener
import com.happy.friendogly.remote.api.Authenticator
import com.happy.friendogly.remote.api.BaseUrl
import com.happy.friendogly.remote.api.ChatService
import com.happy.friendogly.remote.api.ClubService
import com.happy.friendogly.remote.api.MemberService
import com.happy.friendogly.remote.api.MyClubService
import com.happy.friendogly.remote.api.PetService
import com.happy.friendogly.remote.api.PlaygroundService
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

    fun createAuthService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): AuthService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(AuthService::class.java)
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

    fun createMyClubService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): MyClubService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(MyClubService::class.java)
    }

    fun createWoofService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): PlaygroundService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(PlaygroundService::class.java)
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

    fun createChatService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): ChatService {
        return createRetrofit(
            baseUrl,
            tokenManager,
            authenticationListener,
        ).create(ChatService::class.java)
    }

    fun createAlarmTokenService(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): AlarmTokenService {
        return createRetrofit(baseUrl, tokenManager, authenticationListener).create(
            AlarmTokenService::class.java,
        )
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
                authService = createAuthService(baseUrl),
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
        OkHttpClient.Builder().callTimeout(Duration.ofMinutes(TIME_OUT_MINUTE))
            .pingInterval(Duration.ofSeconds(PINT_OUT_SECOND)).apply(interceptors).build()

    fun createStumpClient(
        baseUrl: BaseUrl,
        tokenManager: TokenManager,
        authenticationListener: AuthenticationListener,
    ): StompClient {
        val authenticator =
            Authenticator(
                authService = createAuthService(baseUrl),
                tokenManager = tokenManager,
                authenticationListener = authenticationListener,
            )

        return createOkHttpClient {
            addInterceptor(AuthorizationInterceptor(tokenManager = tokenManager))
            authenticator(authenticator)
            addInterceptor(ErrorResponseInterceptor())
            addInterceptor(logging)
        }.let(::OkHttpWebSocketClient).let(::StompClient)
    }

    private fun createAuthService(baseUrl: BaseUrl): AuthService =
        Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(ErrorResponseInterceptor())
                addInterceptor(logging)
            },
        ).addConverterFactory(json.asConverterFactory(contentType)).build()
            .create(AuthService::class.java)
}
