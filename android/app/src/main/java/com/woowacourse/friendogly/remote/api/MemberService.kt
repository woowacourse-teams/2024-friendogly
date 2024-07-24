package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.request.PostMembersRequest
import com.woowacourse.friendogly.remote.model.response.BaseResponse
import com.woowacourse.friendogly.remote.model.response.MemberResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MemberService {
    @Multipart
    @POST(ApiClient.Member.POST_MEMBER)
    suspend fun postMember(
        @Part("request") body: PostMembersRequest,
        @Part file: MultipartBody.Part?,
    ): BaseResponse<MemberResponse>
}
