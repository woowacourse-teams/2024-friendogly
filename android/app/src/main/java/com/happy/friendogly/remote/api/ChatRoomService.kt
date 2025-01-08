package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.ChatClubMemberResponse
import com.happy.friendogly.remote.model.response.ChatRoomClubResponse
import com.happy.friendogly.remote.model.response.ChatRoomListResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatRoomService {
    @GET(ApiClient.ChatRoom.CHAT_LIST)
    suspend fun getChatList(): BaseResponse<ChatRoomListResponse>

    @GET(ApiClient.ChatRoom.MEMBERS)
    suspend fun getChatMembers(
        @Path("chatRoomId") chatRoomId: Long,
    ): BaseResponse<List<ChatClubMemberResponse>>

    @GET(ApiClient.ChatRoom.CLUB)
    suspend fun getChatClub(
        @Path("chatRoomId") chatRoomId: Long,
    ): BaseResponse<ChatRoomClubResponse>

    @POST(ApiClient.ChatRoom.LEAVE)
    suspend fun postLeaveChatRoom(
        @Path("chatRoomId") chatRoomId: Long,
    )
}
