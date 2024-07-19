package com.woowacourse.friendogly.data.client

import com.woowacourse.friendogly.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val logging =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val client =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
