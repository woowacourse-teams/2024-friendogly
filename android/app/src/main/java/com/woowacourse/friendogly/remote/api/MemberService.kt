package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.request.PostMembersRequest
import com.woowacourse.friendogly.remote.model.response.BaseResponse
import com.woowacourse.friendogly.remote.model.response.MemberResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MemberService {
    @POST(ApiClient.Member.POST_MEMBER)
    suspend fun postMember(
        @Body body: PostMembersRequest,
    ): BaseResponse<MemberResponse>

    @GET(ApiClient.Member.GET_MEMBER_MINE)
    suspend fun getMemberMine(): BaseResponse<MemberResponse>
}
