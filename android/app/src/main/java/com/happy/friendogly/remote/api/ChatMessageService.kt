package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.ChatMessageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface ChatMessageService {
    @GET(ApiClient.ChatMessage.ALL)
    suspend fun getAllChatMessages(
        @Path("chatRoomId") chatRoomId: Long,
    ): BaseResponse<List<ChatMessageResponse>>

    @GET(ApiClient.ChatMessage.TIMES)
    suspend fun getChatMessagesByTime(
        @Path("chatRoomId") chatRoomId: Long,
        @Query("since") since: LocalDateTime,
        @Query("until") until: LocalDateTime,
    ): BaseResponse<List<ChatMessageResponse>>
}
