package com.happy.friendogly.application.di

import com.happy.friendogly.BuildConfig
import com.happy.friendogly.remote.api.AuthService
import com.happy.friendogly.remote.api.Authenticator
import com.happy.friendogly.remote.api.BaseUrl
import com.happy.friendogly.remote.interceptor.AuthorizationInterceptor
import com.happy.friendogly.remote.interceptor.ErrorResponseInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import java.time.Duration
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Base

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Websocket

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val contentType = "application/json".toMediaType()
    private val timeOutMinute = 1L
    private val pingOutSecond = 100L
    private val logging =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    @Base
    fun provideBaseUrl(): BaseUrl = BaseUrl(BuildConfig.base_url)

    @Provides
    @Singleton
    @Websocket
    fun provideWebsocketUrl(): BaseUrl = BaseUrl(BuildConfig.websocket_url)

    @Provides
    @Singleton
    fun provideConverterFactory(): Factory {
        val json =
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        return json.asConverterFactory(contentType)
    }

    @Provides
    @Singleton
    fun provideAuthService(
        @Base baseUrl: BaseUrl,
        converterFactory: Factory,
    ): AuthService =
        Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(ErrorResponseInterceptor())
                addInterceptor(logging)
            },
        ).addConverterFactory(converterFactory).build()
            .create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        @Base baseUrl: BaseUrl,
        authenticator: Authenticator,
        authenticatorInterceptor: AuthorizationInterceptor,
        converterFactory: Factory,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl.url).client(
            createOkHttpClient {
                addInterceptor(authenticatorInterceptor)
                authenticator(authenticator)
                addInterceptor(ErrorResponseInterceptor())
                addInterceptor(logging)
            },
        ).addConverterFactory(converterFactory).build()
    }

    @Singleton
    @Provides
    fun provideStumpClient(
        authenticator: Authenticator,
        authenticatorInterceptor: AuthorizationInterceptor,
    ): StompClient {
        return createOkHttpClient {
            addInterceptor(authenticatorInterceptor)
            authenticator(authenticator)
            addInterceptor(ErrorResponseInterceptor())
            addInterceptor(logging)
        }.let(::OkHttpWebSocketClient).let(
            ::StompClient,
        )
    }

    private fun createOkHttpClient(interceptors: OkHttpClient.Builder.() -> Unit = { }): OkHttpClient =
        OkHttpClient.Builder().callTimeout(Duration.ofMinutes(timeOutMinute))
            .pingInterval(Duration.ofSeconds(pingOutSecond)).apply(interceptors).build()
}
