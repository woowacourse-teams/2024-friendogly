package com.woowacourse.friendogly.remote.retrofit

import com.woowacourse.friendogly.BuildConfig
import com.woowacourse.friendogly.remote.HackathonService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object  DogRetrofit {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient())
            .build()
    }

    val hackathonService: HackathonService = retrofit.create(HackathonService::class.java)
}
