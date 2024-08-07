package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.PostMembersRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.MemberResponse
import com.happy.friendogly.remote.model.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MemberService {
    @Multipart
    @POST(ApiClient.Member.POST_MEMBER)
    suspend fun postMember(
        @Part("request") body: PostMembersRequest,
        @Part file: MultipartBody.Part?,
    ): BaseResponse<RegisterResponse>

    @GET(ApiClient.Member.GET_MEMBER_MINE)
    suspend fun getMemberMine(): BaseResponse<MemberResponse>
}
