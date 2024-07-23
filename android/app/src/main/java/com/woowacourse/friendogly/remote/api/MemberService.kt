package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.request.PostMembersRequest
import com.woowacourse.friendogly.remote.model.response.MemberResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberService {
    @POST(ApiClient.Member.POST_MEMBER)
    suspend fun postMember(
        @Body body: PostMembersRequest,
    ): MemberResponse
}
