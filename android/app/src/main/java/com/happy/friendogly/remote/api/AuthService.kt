package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.PostLoginRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(ApiClient.Auth.POST_KAKAO_LOGIN)
    suspend fun postKakaoLogin(
        @Body body: PostLoginRequest,
    ): BaseResponse<LoginResponse>
}
