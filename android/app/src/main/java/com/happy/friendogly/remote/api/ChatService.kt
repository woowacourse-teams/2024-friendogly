package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.ChatClubMemberResponse
import com.happy.friendogly.remote.model.response.ChatRoomListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService {
    @GET(ApiClient.Chat.CHAT_LIST)
    suspend fun getChatList(): BaseResponse<ChatRoomListResponse>

    @GET(ApiClient.Chat.MEMBERS)
    suspend fun getChatMembers(
        @Path("chatRoomId") chatRoomId: Long,
    ): BaseResponse<List<ChatClubMemberResponse>>
}
